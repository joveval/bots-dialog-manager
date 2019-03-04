package pe.joseval.bots.core;

import static pe.joseval.bots.core.StatesMachineConstants.CUSTOM_SIMPLE_MESSAGE;
import static pe.joseval.util.states.machine.core.StaticMethods.simpleState;

import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.core.context.Context;
import pe.joseval.bots.core.context.ContextClient;
import pe.joseval.bots.core.context.ContextHandler;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingDefinition;
import pe.joseval.bots.dm.response.DialogManagerResponse;
import pe.joseval.util.states.machine.core.Node;
import pe.joseval.util.states.machine.core.State;
import pe.joseval.util.states.machine.core.StatesManager;
import pe.joseval.util.states.machine.core.TransitionResponse;

@Slf4j
public abstract class CoreDialogManager implements DialogManagerInterface {

	private ContextHandler contextHandler;
	private DefaultActions defaultActions = new DefaultActions();
	private StatesManager<DialogManagerResponse> manager = new StatesManager<>();
	private ContextClient contextClient;
	private Configurer configurer = new Configurer();

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
		
		treeRoot.populateStateManager(manager);
	}
	
	
	protected void forceInit() {
		
		initializeWithVariables();
	}

	protected abstract void configure(Configurer configurer);

	@Override
	public DialogManagerResponse getResponseByTransition(Map<String, Object> factParams, String sessionId) {
		
		State currentState = simpleState(0);
		DialogManagerResponse response = null;
		TransitionResponse<DialogManagerResponse> tRes;
		Map<String, Object> smParams;
		Context context;
		ContextHandlingDefinition contextHandDef;

		if (sessionId != null) {

			context = contextClient.findBySessionId(sessionId);
			try {

				Integer stateId = (Integer) context.getMapVariables().get(PARAMS_GLOBAL_STATE_ID);
				currentState = simpleState(stateId);
				factParams.putAll(context.getMapVariables());
			} catch (Exception e) {

				currentState = simpleState(0);
				factParams.put(PARAMS_GLOBAL_STATE_ID, 0);
			}
		} else {

			factParams.put(PARAMS_GLOBAL_STATE_ID, 0);
		}

		tRes = manager.executeTransition(currentState, factParams);
		factParams.put(PARAMS_GLOBAL_STATE_ID, tRes.getNextState().getStateId());
		smParams = tRes.getAction().getCustomParams();

		if (smParams.containsKey(ActionTasks.RUN_CUSTOM_METHOD)) {

			response = (DialogManagerResponse) tRes.getAction().getCustomAction().apply(factParams);
		} else {

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

			if (smParams.containsKey(ActionTasks.HANDLE_CONTEXT)) {

				contextHandDef = (ContextHandlingDefinition) smParams.get(ActionTasks.HANDLE_CONTEXT);
				context = contextHandler.handle(contextHandDef, sessionId, factParams);
				if (context != null) {

					response.setSessionId(context.getSessionId());
				}
			}
		}

		return response;
	}

	protected SimpleMessageCustomizer.SimpleMessageCustomizerBuilder messageCustomizer() {
		
		return SimpleMessageCustomizer.builder();
	}

	protected ContextHandlingDefinition.ContextHandlingDefinitionBuilder contextHandler() {
		
		return ContextHandlingDefinition.builder();
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
