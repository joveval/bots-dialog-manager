package pe.joseval.bots.core;

import pe.joseval.bots.core.context.ContextHandler;
import pe.joseval.util.rules.manager.core.Condition;
import pe.joseval.util.states.machine.core.Edge;

public class DmEdge extends Edge{

	
	
	
	public DmEdge withCondition(Condition condition)
	{
		super.setCondition(condition);
		return this;
	}
	public DmEdge withParamsToPersist(ContextHandler contextHandler) {
		super.getAction().getCustomParams().put(ActionTasks.HANDLE_CONTEXT,contextHandler);
		return this;
	}
	
	
	
}
