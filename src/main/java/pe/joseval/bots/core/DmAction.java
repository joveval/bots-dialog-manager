package pe.joseval.bots.core;

import pe.joseval.bots.core.context.ContextHandler.ContextHandlingDefinition;
import pe.joseval.util.states.machine.core.Action;
import pe.joseval.util.states.machine.core.ActionType;

public class DmAction extends Action {

	public static DmAction action(ActionType actionType) {

		DmAction dmAction = new DmAction();
		dmAction.setActionType(actionType);
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
