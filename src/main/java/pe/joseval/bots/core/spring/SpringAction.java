package pe.joseval.bots.core.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pe.joseval.bots.dm.actions.ContextHandDef;

@Retention(RUNTIME)
@Target(TYPE)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public @interface SpringAction  {
	String name() default "NONE";
	ContextHandDef contextHandDef() default @ContextHandDef;
	boolean automatic() default false;
	boolean forceContextSaving() default false;
}
