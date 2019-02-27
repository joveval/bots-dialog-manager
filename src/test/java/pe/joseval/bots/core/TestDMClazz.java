package pe.joseval.bots.core;


import static pe.joseval.bots.core.DmAction.action;
import static pe.joseval.bots.core.StaticMethods.edge;
import static pe.joseval.bots.core.StaticMethods.node;
import static pe.joseval.bots.core.StaticMethods.simpleState;
import static pe.joseval.util.rules.manager.core.StaticConditions.lEquals;
import static pe.joseval.util.rules.manager.core.StaticConditions.lTrue;

import pe.joseval.bots.core.context.InMemoryContext;
import pe.joseval.util.states.machine.core.ActionType;
import pe.joseval.util.states.machine.core.Node;

public class TestDMClazz extends CoreDialogManager {


	
	
	
	@Override
	protected void configure(Configurer configurer) {
		// TODO Auto-generated method stub
		//log.debug("initTree(Node root)");
		Node root = node(simpleState(0))

				.withEdge(edge(lEquals("intent", "intent_01"))
							
							.withAction(action(ActionType.NAMED_ACTION)
											.withResponseList("Response1")
											.withName("NONE"))
							//.withDirectResponse("Response1")
							.toTarget(node(simpleState(1))))
				.withEdge(edge(lTrue())
							//.withDirectResponse("Response2","Response3")
							.withAction(action(ActionType.NAMED_ACTION)
									.withResponseList("Response2","Response3")
									.withName("NONE")
									)
							.toTarget(node(simpleState(2))));
		
		configurer.withContextClient(new InMemoryContext())
				  .withTreeRoot(root);
	}
	
	

}
