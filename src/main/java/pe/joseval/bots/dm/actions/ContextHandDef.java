package pe.joseval.bots.dm.actions;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import pe.joseval.bots.core.context.ContextHandler.ContextHandlingTypes;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ContextHandDef {
	ContextHandlingTypes type() default ContextHandlingTypes.NONE;
	String[] params() default {};
}
