package pe.joseval.bots.core;

import lombok.Builder;
import pe.joseval.bots.dm.response.ResponseType;

@Builder
public class SimpleMessageCustomizer {

	private String paramToFill;
	private String[] contextToSave;
	private ResponseType responseType;
	
	public SimpleMessageCustomizer() {
	}

	public SimpleMessageCustomizer(String paramToFill, String[] contextToSave, ResponseType responseType) {
		this.paramToFill = paramToFill;
		this.contextToSave = contextToSave;
		this.responseType = responseType;
	}
	
	public String getParamToFill() {
		return paramToFill;
	}
	public void setParamToFill(String paramToFill) {
		this.paramToFill = paramToFill;
	}
	public String[] getContextToSave() {
		return contextToSave;
	}
	public void setContextToSave(String[] contextToSave) {
		this.contextToSave = contextToSave;
	}
	public ResponseType getResponseType() {
		return responseType;
	}
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}
	
	
	
	
	
	
	

}
