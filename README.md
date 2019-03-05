# Chatbot dialog manager

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4d329240714643afad71b06c83d881d8)](https://app.codacy.com/app/joveval/bots-dialog-manager?utm_source=github.com&utm_medium=referral&utm_content=joveval/bots-dialog-manager&utm_campaign=Badge_Grade_Dashboard)

Chatbot Dialog Manager is a java package that allows to model a dialog as a state machine. The advantage of using this package is that gives you all the boilerplate code needed to define a sequence of dialog in a interactive and easy way (as dialog tree).

## Installation
Clone this project in your PC. Then install it using Maven:

``` batch
./mvn install
```
## POM reference

``` xml
<dependency>
	<groupId>io.github.joveval.bots</groupId>
	<artifactId>ChatbotDialogManager</artifactId>
	<version>1.1.7-beta</version>
</dependency>
```
## Use
Define the dialog tree sequence:

*   Create a class that extends to `CoreDialogManager`:

``` java
public class MyDialogManager extends CoreDialogManager {

	@Override
	protected void configure(Configurer configurer) {

		Node root = node(simpleState(0))
				.withEdge(edge(lEquals("intent", "intent_01"))
								.withAction(action(ActionType.NAMED_ACTION)
									.withResponseList("Response1")
									.withName("NONE"))
							.toTarget(node(simpleState(1))))
				.withEdge(edge(lTrue())
								.withAction(action(ActionType.NAMED_ACTION)
									.withResponseList("Response2","Response3")
									.withName("NONE"))
							.toTarget(node(simpleState(2))));
		
		configurer.withContextClient(new InMemoryContext())
				  	 .withTreeRoot(root);
	}

}
```
You could define an in memory context storage or implement your own. Specify it using `withContextClient`. 

*   Populate a map of values:

``` java
MyDialogManager sm = new MyDialogManager();
sm.forceInit();
Map<String, Object> factParams = new HashMap<>();
factParams.put("intent", "intent_01");
```
*   Execute transition as needed:

``` java
DialogManagerResponse resp = sm.getResponseByTransition(factParams, null);
```

## License
MIT - See [LICENSE](https://github.com/joveval/bots-dialog-manager/blob/master/LICENSE) for more details.
