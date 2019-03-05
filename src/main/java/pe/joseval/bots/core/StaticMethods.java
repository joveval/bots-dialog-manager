package pe.joseval.bots.core;

import pe.joseval.util.rules.manager.core.Condition;
import pe.joseval.util.states.machine.core.Node;
import pe.joseval.util.states.machine.core.State;

public class StaticMethods {

	public static Node node(State rootState) {
		
		return pe.joseval.util.states.machine.core.StaticMethods.node(rootState);
	}
	
	public static Node node(int stateId) {
		
		return pe.joseval.util.states.machine.core.StaticMethods.node(pe.joseval.util.states.machine.core.StaticMethods.simpleState(stateId));
	}

	public static DmEdge edge() {
		
		return new DmEdge();
	}
	
	public static DmEdge edge(Condition condition) {
		
		DmEdge edge = new DmEdge();
		edge.setCondition(condition);
		return edge;
	}
	

	public static State simpleState(int stateId) {

		return pe.joseval.util.states.machine.core.StaticMethods.simpleState(stateId);
	}
	
}
