package pe.joseval.bots.core;

import static pe.joseval.bots.core.StaticMethods.edge;
import static pe.joseval.bots.core.StaticMethods.node;
import static pe.joseval.bots.core.StaticMethods.simpleState;
import static pe.joseval.bots.dm.actions.DmAction.action;
import static pe.joseval.util.rules.manager.core.StaticConditions.lEquals;
import static pe.joseval.util.rules.manager.core.StaticConditions.lTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.core.context.InMemoryContext;
import pe.joseval.bots.core.spring.SpringCoreDialogManager;
import pe.joseval.util.states.machine.core.Node;
@Slf4j
@Component
@ActionsScan("pe.joseval.bots.core")
public class TestDMSpringClazz extends SpringCoreDialogManager{

	@Autowired
	protected TestDMSpringClazz(ApplicationContext springContext) {
		super(springContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure(Configurer configurer) {
		// TODO Auto-generated method stub
		log.debug("Init creating dialog tree.");
		Node root = node(0)

				.withEdge(edge(lEquals("intent", "intent_01"))
							.withAction(action("ACTION01"))
							.toTarget(node(simpleState(1))))
				.withEdge(edge(lTrue())
							.withAction(action("ACTION_DEFAULT"))
							.toTarget(node(2)));
		
		configurer.withContextClient(new InMemoryContext())
				  .withTreeRoot(root);
		log.debug("finish creating dialog tree.");
	}

}
