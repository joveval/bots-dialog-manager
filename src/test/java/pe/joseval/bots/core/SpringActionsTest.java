package pe.joseval.bots.core;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingTypes;
import pe.joseval.bots.core.spring.SpringAction;
import pe.joseval.bots.dm.actions.BaseAction;
import pe.joseval.bots.dm.actions.ContextHandDef;
import pe.joseval.bots.dm.response.DialogManagerResponse;

@Slf4j
public class SpringActionsTest {

	@SpringAction(name="ACTION01",contextHandDef=@ContextHandDef(type=ContextHandlingTypes.SAVE_PARTIALLY,params= {"stateId"}))
	public static class Action01 implements BaseAction<DialogManagerResponse>{

		@Override
		public DialogManagerResponse execute(Map<String, Object> params) {
			// TODO Auto-generated method stub
			log.debug("DENTRO DE ACTION01");
			DefaultActions da = new DefaultActions();
			return da.getPlainResponseFromList(new String[] {"RESPONSEACTION01"});
		}
		
	}
	
	
	@SpringAction(name = "ACTION_DEFAULT", contextHandDef = @ContextHandDef(type = ContextHandlingTypes.NONE))
	public static class ActionDefault implements BaseAction<DialogManagerResponse> {
		@Override

		public DialogManagerResponse execute(Map<String, Object> params) {
			// TODO Auto-generated method stub
			log.debug("DENTRO DE ACTION01");
			DefaultActions da = new DefaultActions();
			return da.getPlainResponseFromList(new String[] {"RESPONSEACTION_DEFAULT"});
		}

	}
	
	
	
}
