package pe.joseval.bots.core.context;

public interface ContextClient {

	
	Context findContext(String id);
	
	Context findBySessionId( String sessionId);
	
	Context updateContext( String sessionId,Context context);
	
	void deleteContext(String id);
	
	Context save( Context context);
	
	
	
}