package pe.joseval.bots.core;

import static pe.joseval.bots.core.StatesMachineConstants.CUSTOM_SIMPLE_MESSAGE;
import static pe.joseval.util.states.machine.core.StaticMethods.simpleState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.core.context.Context;
import pe.joseval.bots.core.context.ContextClient;
import pe.joseval.bots.core.context.ContextHandler;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingDefinition;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingTypes;
import pe.joseval.bots.dm.actions.Action;
import pe.joseval.bots.dm.actions.BaseAction;
import pe.joseval.bots.dm.actions.ContextHandDef;
import pe.joseval.bots.dm.response.DialogManagerResponse;
import pe.joseval.util.states.machine.core.ActionType;
import pe.joseval.util.states.machine.core.Node;
import pe.joseval.util.states.machine.core.State;
import pe.joseval.util.states.machine.core.StatesManager;
import pe.joseval.util.states.machine.core.TransitionResponse;

/**
 * @author Usuario
 *
 */
@Slf4j
public abstract class CoreDialogManager implements DialogManagerInterface {

	private ContextHandler contextHandler;
	private DefaultActions defaultActions = new DefaultActions();
	private StatesManager<DialogManagerResponse> manager = new StatesManager<>();
	private ContextClient contextClient;
	private Configurer configurer = new Configurer();
	private Map<String, Object> namedActions = new HashMap<>();

	protected CoreDialogManager() {

	}

	@PostConstruct
	private void initializeWithVariables() {

		Node treeRoot;

		log.debug("Configuring dialog manager");
		configure(configurer);
		contextClient = configurer.getContextClient();

		if (contextClient == null)
			log.warn("ContextClient has not been defined. It may be cumbersome if you try to save context values.");

		contextHandler = new ContextHandler(contextClient);
		treeRoot = configurer.getTreeRoot();

		if (treeRoot == null)
			log.error("Not possible to find a conversational tree definition.");

		String basePackageToScan = null;

		log.debug("this = {}", this.getClass().getName());
		ActionsScan anno = this.getClass().getDeclaredAnnotation(ActionsScan.class);

		if (anno != null) {
			basePackageToScan = anno.value() == null ? anno.basePackage() : anno.value();

			log.debug("Searching for Actions classes in {}", basePackageToScan);
		}
		// String basePackageToScan = ((ActionsScan)anno).basePackage();
		scanForNamedActions(basePackageToScan);

		treeRoot.populateStateManager(manager);
	}

	protected void scanForNamedActions(String basePackageToScan) {
		if (basePackageToScan != null) {
			Reflections reflections = new Reflections(basePackageToScan);
			Set<Class<?>> localActions = reflections.getTypesAnnotatedWith(Action.class);
			localActions = localActions.stream().filter(c -> {
				List<Class<?>> l = Arrays.asList(c.getInterfaces());
				l = l.stream().filter(a -> a.equals(BaseAction.class)).collect(Collectors.toList());
				return !l.isEmpty();
			}).collect(Collectors.toSet());

			log.debug("Listing actions to invoke in dialog management ...");
			localActions.forEach(a -> {

				Action ann = a.getAnnotation(Action.class);
				log.debug("Named Action = name = {},context = {},automatic = {}", ann.name(),
						ann.contextHandDef().type(), ann.automatic());
				try {
					namedActions.put(ann.name(), a.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					log.error("ERROR:", e);
				}
			});

		}
	}

	protected void forceInit() {

		initializeWithVariables();
	}

	public Map<String, Object> getNamedActions() {
		return namedActions;
	}

	protected abstract void configure(Configurer configurer);

	@Override
	public DialogManagerResponse getResponseByTransition(Map<String, Object> factParams, String sessionId) {

		State currentState = simpleState(0);
		DialogManagerResponse response = null;
		TransitionResponse<DialogManagerResponse> tRes;
		Map<String, Object> smParams;

		// Setting current state
		currentState = getStateBasedOnSessionId(sessionId);
		factParams.put(PARAMS_GLOBAL_STATE_ID, currentState.getStateId());
		// Execute state machine transition
		tRes = manager.executeTransition(currentState, factParams);
		// Saving next state in fact params
		factParams.put(PARAMS_GLOBAL_STATE_ID, tRes.getNextState().getStateId());
		smParams = tRes.getAction().getCustomParams();

		pe.joseval.bots.dm.actions.ActionType localActionType = null;
		if (smParams.containsKey(StatesMachineConstants.ACTION_TYPE))
			localActionType = (pe.joseval.bots.dm.actions.ActionType) smParams.get(StatesMachineConstants.ACTION_TYPE);

		ActionType actionType = tRes.getAction().getActionType();

		if (actionType == null && localActionType == null) {
			log.info("No action type defined so defaulting to null response value.");
			return null;
		}

		if (actionType != null && localActionType != null) {
			log.info("Both types of action can not be defined at the same time, so defaulting to null response value.");
			return null;
		}

		if (actionType != null) {
			if (actionType.equals(ActionType.LAMBDA_ACTION))
				response = (DialogManagerResponse) tRes.getAction().getCustomAction().apply(factParams);
			else
				response = automaticResponse(smParams);

			if (smParams.containsKey(ActionTasks.HANDLE_CONTEXT))
				makeContextTask(smParams, factParams, sessionId, response);

		} else {

			switch (localActionType) {
			case AUTO:
				response = automaticResponse(smParams);
				if (smParams.containsKey(ActionTasks.HANDLE_CONTEXT)) 
					makeContextTask(smParams, factParams, sessionId, response);
				break;
			case LAMBDA:
				response = (DialogManagerResponse) tRes.getAction().getCustomAction().apply(factParams);
				if (smParams.containsKey(ActionTasks.HANDLE_CONTEXT)) 
					makeContextTask(smParams, factParams, sessionId, response);
				
				break;
			case NAMED:

				if (tRes.getAction().getActionToMake() != null) {
					Object actionAbs = namedActions.get(tRes.getAction().getActionToMake());
					if (actionAbs != null) {
						try {
							// actionAbs.
							@SuppressWarnings("unchecked")
							BaseAction<DialogManagerResponse> bAction = (BaseAction<DialogManagerResponse>) actionAbs;

							response = bAction.execute(factParams);

							ContextHandDef contextHD = getContextHandAnnotation(bAction);
							if (!contextHD.type().equals(ContextHandlingTypes.NONE)) {
								ContextHandlingDefinition contextHandlingDefinition = new ContextHandlingDefinition();
								contextHandlingDefinition.setParamsToHandle(contextHD.params());
								contextHandlingDefinition.setType(contextHD.type());
								useContextHandler(contextHandlingDefinition, sessionId, response, factParams);
							}

						} catch (ClassCastException e) {
							// TODO Auto-generated catch block
							log.error("ERROR:", e);
						}
					} else {
						log.warn("Not founded Action class with name: {}", tRes.getAction().getActionToMake());
					}
				} else {
					log.warn("Defined as named but not correctly set.");
				}

				break;
			default:
				log.debug("This is a possible bug in CoreDialogManager");
				break;

			}

		}

		return response;
	}

	private State getStateBasedOnSessionId(String sessionId) {
		Context context;
		if (sessionId != null) {

			context = contextClient.findBySessionId(sessionId);
			try {

				Integer stateId = (Integer) context.getMapVariables().get(PARAMS_GLOBAL_STATE_ID);
				return simpleState(stateId);
			} catch (Exception e) {

				return simpleState(0);
			}
		} else {
			return simpleState(0);

		}
	}

	protected ContextHandDef getContextHandAnnotation(Object actionClazz) {
		Action ann = actionClazz.getClass().getAnnotation(Action.class);
		return ann.contextHandDef();
	}

	protected SimpleMessageCustomizer.SimpleMessageCustomizerBuilder messageCustomizer() {

		return SimpleMessageCustomizer.builder();
	}

	protected ContextHandlingDefinition.ContextHandlingDefinitionBuilder contextHandler() {

		return ContextHandlingDefinition.builder();
	}

	private DialogManagerResponse automaticResponse(Map<String, Object> smParams) {

		DialogManagerResponse response = null;
		if (smParams.containsKey(ActionTasks.BUILD_RESPONSE_MESSAGE)) {

			String[] directList = (String[]) smParams.get(ActionTasks.BUILD_RESPONSE_MESSAGE);
			if (smParams.containsKey(CUSTOM_SIMPLE_MESSAGE)) {

				SimpleMessageCustomizer customizer = (SimpleMessageCustomizer) smParams.get(CUSTOM_SIMPLE_MESSAGE);
				response = defaultActions.getPlainResponseFromList(directList, customizer);
			} else {

				response = defaultActions.getPlainResponseFromList(directList);
			}
		}

		if (smParams.containsKey(ActionTasks.BUILD_ORDERED_RESPONSE_MESSAGES)) {

			String[] orderedList = (String[]) smParams.get(ActionTasks.BUILD_ORDERED_RESPONSE_MESSAGES);
			if (smParams.containsKey(CUSTOM_SIMPLE_MESSAGE)) {

				SimpleMessageCustomizer customizer = (SimpleMessageCustomizer) smParams.get(CUSTOM_SIMPLE_MESSAGE);
				response = defaultActions.getSimpleMultilineMessage(orderedList, customizer);
			} else {

				response = defaultActions.getSimpleMultilineMessage(orderedList);
			}

		}

		return response;
	}

	private void makeContextTask(final Map<String, Object> smParams, final Map<String, Object> factParams,
			String sessionId, DialogManagerResponse response) {
		// DialogManagerResponse response = null;
		ContextHandlingDefinition contextHandDef;
		contextHandDef = (ContextHandlingDefinition) smParams.get(ActionTasks.HANDLE_CONTEXT);
		useContextHandler(contextHandDef, sessionId, response, factParams);
	}

	private void useContextHandler(ContextHandlingDefinition contextHandDef, String sessionId,
			DialogManagerResponse response, Map<String, Object> factParams) {
		Context localContext = contextHandler.handle(contextHandDef, sessionId, factParams);
		if (localContext != null)
			response.setSessionId(localContext.getSessionId());

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(builderClassName = "Builder", builderMethodName = "builder")
	public static class Configurer {

		private ContextClient contextClient;
		private Node treeRoot;

		public Configurer withContextClient(ContextClient contextClient) {
			this.contextClient = contextClient;
			return this;
		}

		public Configurer withTreeRoot(Node treeRoot) {
			this.treeRoot = treeRoot;
			return this;
		}
	}

}
