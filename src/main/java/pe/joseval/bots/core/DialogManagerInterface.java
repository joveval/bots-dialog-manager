package pe.joseval.bots.core;

import java.util.Map;

import pe.joseval.bots.dm.response.DialogManagerResponse;

public interface DialogManagerInterface {

	public static String PARAMS_GLOBAL_STATE_ID="stateId";
	
	
	public DialogManagerResponse getResponseByTransition(Map<String, Object> factParams,
			String sessionId);
}
