package pe.joseval.bots.core;

import pe.joseval.util.states.machine.core.Action;
import pe.joseval.util.states.machine.core.ActionType;

public class DmAction extends Action{

	
	public static DmAction action(ActionType actionType) {
		DmAction dmAction = new DmAction();
		dmAction.setActionType(actionType);
		return dmAction;
	}
	
	
	public DmAction withResponseList(String ...posibleMessages) {
		super.getCustomParams().put(ActionTasks.BUILD_RESPONSE_MESSAGE, posibleMessages);
		return this;
	}
}
