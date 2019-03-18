package pe.joseval.bots.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static pe.joseval.bots.core.StaticMethods.edge;
import static pe.joseval.bots.core.StaticMethods.node;
import static pe.joseval.bots.dm.actions.DmAction.action;
import static pe.joseval.util.rules.manager.core.StaticConditions.lEquals;
import static pe.joseval.util.rules.manager.core.StaticConditions.lTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import pe.joseval.bots.core.context.ContextHandler.ContextHandlingTypes;
import pe.joseval.bots.core.context.InMemoryContext;
import pe.joseval.bots.dm.response.DialogManagerResponse;
import pe.joseval.util.states.machine.core.ActionType;
import pe.joseval.util.states.machine.core.Node;

public class DialogManagerTest {

	@Test
	public void test0() {

		TestDMClazzTest testClazz = new TestDMClazzTest();
		testClazz.forceInit();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		DialogManagerResponse resp = testClazz.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response1"));
	}

	@Test
	public void test2() {

		TestDMClazzTest testClazz = new TestDMClazzTest();
		testClazz.forceInit();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_0");
		DialogManagerResponse resp = testClazz.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response2")
				|| resp.getMessages().get(0).getMessageBody().equals("Response3"));
	}

	@Test
	public void test3() {

		CoreDialogManager sm = new CoreDialogManager() {

			@Override
			protected void configure(Configurer configurer) {
				Node root = node(0)

						.withEdge(edge(lEquals("intent", "intent_01"))

								.withAction(action(ActionType.LAMBDA_ACTION).withName("NONE")
										.withCustomAction(new Function<Map<String, Object>, DialogManagerResponse>() {

											@Override
											public DialogManagerResponse apply(Map<String, Object> t) {
												// TODO Auto-generated method stub
												DefaultActions da = new DefaultActions();
												DialogManagerResponse response = da.getPlainResponseFromList(
														new String[] { "Response1", "Response0" });
												return response;
											}

										}))
								.toTarget(node(1)))
						.withEdge(
								edge(lTrue())
										.withAction(action(ActionType.NAMED_ACTION)
												.withResponseList("Response2", "Response3").withName("NONE"))
										.toTarget(node(2)));

				configurer.withContextClient(new InMemoryContext()).withTreeRoot(root);
			}

		};

		sm.forceInit();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		DialogManagerResponse resp = sm.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response1")
				|| resp.getMessages().get(0).getMessageBody().equals("Response0"));
	}

	@Test
	public void testInMemoryContext() {

		CoreDialogManager sm = new CoreDialogManager() {

			@Override
			protected void configure(Configurer configurer) {
				Node root = node(0)

						.withEdge(edge(lEquals("intent", "intent_01"))

								.withAction(action(ActionType.LAMBDA_ACTION)
										.withContextHandlingTask(
												contextHandler().paramsToHandle(new String[] { PARAMS_GLOBAL_STATE_ID })
														.type(ContextHandlingTypes.SAVE_PARTIALLY).build())
										.withName("NONE")
										.withCustomAction(new Function<Map<String, Object>, DialogManagerResponse>() {

											@Override
											public DialogManagerResponse apply(Map<String, Object> t) {
												// TODO Auto-generated method stub
												DefaultActions da = new DefaultActions();
												DialogManagerResponse response = da.getPlainResponseFromList(
														new String[] { "Response1", "Response0" });
												return response;
											}

										}))
								.toTarget(node(1).withEdge(edge(lEquals("intent", "intent_011"))
										.withAction(action(ActionType.LAMBDA_ACTION).withName("NONE").withCustomAction(
												new Function<Map<String, Object>, DialogManagerResponse>() {

													@Override
													public DialogManagerResponse apply(Map<String, Object> t) {
														// TODO Auto-generated method stub
														DefaultActions da = new DefaultActions();
														DialogManagerResponse response = da.getPlainResponseFromList(
																new String[] { "Response4", "Response5" });
														return response;
													}

												}))
										.toTarget(node(11)))
										.withEdge(edge(lTrue()).withAction(action(ActionType.NAMED_ACTION)
												.withResponseList("Response101", "Response102").withName("NONE"))
												.toTarget(node(10)))))
						.withEdge(
								edge(lTrue())
										.withAction(action(ActionType.NAMED_ACTION)
												.withResponseList("Response2", "Response3").withName("NONE"))
										.toTarget(node(2)));

				configurer.withContextClient(new InMemoryContext()).withTreeRoot(root);
				
				
				
				
				
			}

		};
		
		
		sm.forceInit();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		DialogManagerResponse resp = sm.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response1")
				|| resp.getMessages().get(0).getMessageBody().equals("Response0"));

		assertNotNull(resp.getSessionId());
		factParams.replace("intent", "intent_011");
		
		
		resp = sm.getResponseByTransition(factParams, resp.getSessionId());
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("Response4")
				|| resp.getMessages().get(0).getMessageBody().equals("Response5"));
		
		
		
	}
	
	
	@Test
	public void namedActionsTest() {
		NamedActionDMTest sm = new NamedActionDMTest();
		sm.forceInit();
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("intent", "intent_01");
		
		DialogManagerResponse resp = sm.getResponseByTransition(factParams, null);
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("RESPONSEACTION01"));
		
		factParams.replace("intent", "intent_xx");
		
		resp = sm.getResponseByTransition(factParams, null);
		
		assertNotNull(resp);
		assertFalse(resp.getMessages().isEmpty());
		assertNotNull(resp.getMessages().get(0).getMessageBody());
		assertTrue(resp.getMessages().get(0).getMessageBody().equals("RESPONSEACTION_DEFAULT"));
		
	}
	
	

}
