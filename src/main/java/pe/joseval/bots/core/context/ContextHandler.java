package pe.joseval.bots.core.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextHandler {

	private ContextClient contextClient;
	
	public ContextHandler(ContextClient contextClient) {
		this.contextClient = contextClient;
	}
	
	protected void setContextClient(ContextClient contextClient) {
		this.contextClient = contextClient;
	}
	
	public Context handle(ContextHandlingDefinition definition, String sessionId, Map<String, Object> factParams) {

		Context context = null;
		Context savedContext; 
		Context findedContext;
		String contextId = null;
		
		if (definition.getType() != null) {
			
			log.info("[CONTEXT][HANDLING]: Type = {}",definition.getType());
			switch (definition.getType()) {
			case SAVE_ALL:
				if (sessionId == null) {
					context = new Context();
					context.setMapVariables(factParams);
					savedContext = contextClient.save(context);
				} else {
					findedContext = contextClient.findBySessionId(sessionId);
					//contextId = getStringIdFromUrl(findedContext.getId().getHref());
					context = findedContext;
					context.getMapVariables().putAll(factParams);
					savedContext = contextClient.updateContext(contextId, context);
				}

				context = savedContext;
				break;
			case SAVE_PARTIALLY:

				Map<String, Object> netFactParams = new HashMap<>();
				for (String var : definition.getParamsToHandle()) 
					netFactParams.put(var, factParams.get(var));
				
				if (sessionId == null) {
					context = new Context();
					context.setMapVariables(netFactParams);
					savedContext = contextClient.save(context);
				} else {
					findedContext = contextClient.findBySessionId(sessionId);
					context = findedContext;
					context.getMapVariables().putAll(netFactParams);
					savedContext = contextClient.updateContext(sessionId, context);
				}

				context = savedContext;

				break;
			case UPDATE:
				// Not necessary yet.
				break;
			case DELETE_ALL:
				context = null;
				break;
			case DELETE_PARTIALLY:

				findedContext = contextClient.findBySessionId(sessionId);
				//contextId = getStringIdFromUrl(findedContext.getId().getHref());
				context = findedContext;
				List<String> arrayAsList = Arrays.asList(definition.getParamsToHandle());

				for (String key : factParams.keySet().stream().filter(a -> arrayAsList.contains(a))
						.collect(Collectors.toList())) context.getMapVariables().remove(key);
				
				savedContext = contextClient.updateContext(sessionId, context);
				context = savedContext;
				break;
			default:

			}
		} else {
			log.info("[CONTEXT][HANDLING]: Type = NONE");
			if (sessionId != null) {
				try {
					context = contextClient.findBySessionId(sessionId);
				} catch (Exception e) {
					log.warn("Context couldn't be obtained. It could be a time out or not found.");
				}
			}
		}

		log.info("[CONTEXT][HANDLING]: Result = {}",context);
		return context;

	}

	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ContextHandlingDefinition {

		private ContextHandlingTypes type;
		private String[] paramsToHandle;
	}

	
	
	@Getter
	@AllArgsConstructor
	public static enum ContextHandlingTypes {
		UPDATE(0),
		DELETE_PARTIALLY(1),
		DELETE_ALL(2),
		SAVE_ALL(3),
		SAVE_PARTIALLY(4),
		NONE(5);
		private int value;
	}
	
}
