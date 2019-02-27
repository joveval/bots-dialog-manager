package pe.joseval.bots.core.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryContext implements ContextClient {

	private Map<String, Context> contextList;

	public InMemoryContext() {
		this.contextList = new HashMap<>();
	}

	@Override
	public Context findContext(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context findBySessionId(String sessionId) {
		// TODO Auto-generated method stub
		if (contextList.containsKey(sessionId)) {
			return contextList.get(sessionId);
		}
		return null;
	}

	@Override
	public Context updateContext(String sessionId, Context context) {
		// TODO Auto-generated method stub
		if (contextList.containsKey(sessionId)) {

			contextList.replace(sessionId, context);
			return contextList.get(sessionId);
		}
		return null;
	}

	@Override
	public void deleteContext(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context save(Context context) {
		// TODO Auto-generated method stub

		String sessionId = UUID.randomUUID().toString();
		contextList.put(sessionId, context);
		return contextList.get(sessionId);
	}

}
