package pe.joseval.bots.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static pe.joseval.bots.core.DmAction.action;
import static pe.joseval.bots.core.StaticMethods.edge;
import static pe.joseval.bots.core.StaticMethods.node;
import static pe.joseval.bots.core.StaticMethods.simpleState;
import static pe.joseval.util.rules.manager.core.StaticConditions.lEquals;
import static pe.joseval.util.rules.manager.core.StaticConditions.lTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import pe.joseval.bots.core.context.InMemoryContext;
import pe.joseval.bots.dm.response.DialogManagerResponse;
import pe.joseval.util.states.machine.core.ActionType;
import pe.joseval.util.states.machine.core.Node;
public class DialogManagerTest {

	
	
	@Test
	public void test0() {

		TestDMClazz testClazz = new TestDMClazz();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		//State state = SimpleState(0);
		DialogManagerResponse resp = testClazz.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response1"));
	}
	
	
	@Test
	public void test2() {

		TestDMClazz testClazz = new TestDMClazz();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_0");
		//State state = SimpleState(0);
		DialogManagerResponse resp = testClazz.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue( resp.getMessages().get(0).getMessageBody().equals("Response2")||resp.getMessages().get(0).getMessageBody().equals("Response3"));
	}
	
	
	@Test
	public void test3(){
		CoreDialogManager sm = new CoreDialogManager() {

			@Override
			protected void configure(Configurer configurer) {
				Node root = node(simpleState(0))

						.withEdge(edge(lEquals("intent", "intent_01"))
									
									.withAction(action(ActionType.LAMBDA_ACTION)
													.withResponseList("Response1")
													.withName("NONE")
													.withCustomAction(new Function<Map<String,Object>, DialogManagerResponse>() {

														@Override
														public DialogManagerResponse apply(Map<String, Object> t) {
															// TODO Auto-generated method stub
															DefaultActions da = new DefaultActions();
															DialogManagerResponse response = da.getPlainResponseFromList(new String[] {"Response1","Response0"});
															return response;
														}
														
														
													}))
									.toTarget(node(simpleState(1))))
						.withEdge(edge(lTrue())
									.withAction(action(ActionType.NAMED_ACTION)
											.withResponseList("Response2","Response3")
											.withName("NONE")
											)
									.toTarget(node(simpleState(2))));
				
				configurer.withContextClient(new InMemoryContext())
						  .withTreeRoot(root);
			}
			
		};
		
		
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		DialogManagerResponse resp = sm.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue( resp.getMessages().get(0).getMessageBody().equals("Response1")||resp.getMessages().get(0).getMessageBody().equals("Response0"));
	}
	
	

}
