package pe.joseval.bots.core;

import static pe.joseval.bots.core.StaticMethods.edge;
import static pe.joseval.bots.core.StaticMethods.node;
import static pe.joseval.bots.core.StaticMethods.simpleState;
import static pe.joseval.bots.dm.actions.DmAction.action;
import static pe.joseval.util.rules.manager.core.StaticConditions.lEquals;
import static pe.joseval.util.rules.manager.core.StaticConditions.lTrue;

import pe.joseval.bots.core.context.InMemoryContext;
import pe.joseval.util.states.machine.core.Node;

@ActionsScan("pe.joseval.bots.core.actions.test")
public class NamedActionDMTest extends CoreDialogManager {

	@Override
	protected void configure(Configurer configurer) {
		// TODO Auto-generated method stub
		Node root = node(0)

				.withEdge(edge(lEquals("intent", "intent_01"))
							.withAction(action("ACTION01"))
							.toTarget(node(simpleState(1))))
				.withEdge(edge(lTrue())
							.withAction(action("ACTION_DEFAULT"))
							.toTarget(node(2)));
		
		configurer.withContextClient(new InMemoryContext())
				  .withTreeRoot(root);
	}

}
