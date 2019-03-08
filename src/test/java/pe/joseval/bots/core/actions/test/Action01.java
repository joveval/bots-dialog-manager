package pe.joseval.bots.core.actions.test;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.core.DefaultActions;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingTypes;
import pe.joseval.bots.dm.actions.Action;
import pe.joseval.bots.dm.actions.BaseAction;
import pe.joseval.bots.dm.actions.ContextHandDef;
import pe.joseval.bots.dm.response.DialogManagerResponse;

@Action(name="ACTION01",contextHandDef=@ContextHandDef(type=ContextHandlingTypes.SAVE_PARTIALLY,params= {"stateId"}))
@Slf4j
public class Action01 implements BaseAction<DialogManagerResponse>{

	@Override
	public DialogManagerResponse execute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		log.debug("DENTRO DE ACTION01");
		DefaultActions da = new DefaultActions();
		return da.getPlainResponseFromList(new String[] {"RESPONSEACTION01"});
	}

	
}
