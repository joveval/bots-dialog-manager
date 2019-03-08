package pe.joseval.bots.dm.actions;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Action {
	String name() default "NONE";
	ContextHandDef contextHandDef() default @ContextHandDef;
	boolean automatic() default false;
	boolean forceContextSaving() default false;
}
