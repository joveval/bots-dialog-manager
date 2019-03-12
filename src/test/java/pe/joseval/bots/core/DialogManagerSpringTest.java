package pe.joseval.bots.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pe.joseval.bots.dm.response.DialogManagerResponse;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DialogManagerSpringTest {

	@Autowired
	TestDMSpringClazz sm;
	
	@Test
	public void test() {
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
