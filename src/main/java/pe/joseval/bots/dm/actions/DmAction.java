package pe.joseval.bots.dm.actions;

import pe.joseval.bots.core.ActionTasks;
import pe.joseval.bots.core.SimpleMessageCustomizer;
import pe.joseval.bots.core.StatesMachineConstants;
import pe.joseval.bots.core.context.ContextHandler.ContextHandlingDefinition;
import pe.joseval.util.states.machine.core.Action;
import pe.joseval.util.states.machine.core.ActionType;

public class DmAction extends Action {

	public static DmAction action(ActionType actionType) {

		DmAction dmAction = new DmAction();
		dmAction.setActionType(actionType);
		return dmAction;
	}
	
	
	public static DmAction action(pe.joseval.bots.dm.actions.ActionType actionType) {
		DmAction dmAction = new DmAction();
		dmAction.getCustomParams().put(StatesMachineConstants.ACTION_TYPE, actionType);
		return dmAction;
	}
	
	public static DmAction action(String name) {
		DmAction dmAction = new DmAction();
		dmAction.setActionToMake(name);
		dmAction.getCustomParams().put(StatesMachineConstants.ACTION_TYPE, pe.joseval.bots.dm.actions.ActionType.NAMED);
		return dmAction;
	}
	
	public DmAction withResponseList(String... posibleMessages) {

		super.getCustomParams().put(ActionTasks.BUILD_RESPONSE_MESSAGE, posibleMessages);
		return this;
	}
	
	public DmAction withOrderedResponseList(String ... messages) {
		
		super.getCustomParams().put(ActionTasks.BUILD_ORDERED_RESPONSE_MESSAGES, messages);
		return this;
	}

	public DmAction withContextHandlingTask(ContextHandlingDefinition def) {

		super.getCustomParams().put(ActionTasks.HANDLE_CONTEXT, def);
		return this;
	}
	
	public DmAction withResponseStyleCustomizer(SimpleMessageCustomizer customizer) {
		
		super.getCustomParams().put(ActionTasks.CUSTOMIZE_DEFAULT_MESSAGE, customizer);
		return this;
	}
}
