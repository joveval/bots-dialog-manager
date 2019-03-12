package pe.joseval.bots.core.spring;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import pe.joseval.bots.core.CoreDialogManager;
import pe.joseval.bots.dm.actions.BaseAction;

public abstract class SpringCoreDialogManager extends CoreDialogManager {

	private final ApplicationContext springContext;
	
	
	protected  SpringCoreDialogManager(ApplicationContext springContext) {
		// TODO Auto-generated constructor stub
		this.springContext = springContext;
	}
	
	
	@Override
	protected void scanForNamedActions(String basePackageToScan) {
		// TODO Auto-generated method stub
		
		if(basePackageToScan!=null) {
			Map<String, Object> annotatedComponents = springContext.getBeansWithAnnotation(SpringAction.class);
			annotatedComponents = annotatedComponents.entrySet().stream().filter(ks->{
				Class<?> c = ks.getValue().getClass();
				List<Class<?>> l = Arrays.asList(c.getInterfaces());
				l = l.stream().filter(a -> a.equals(BaseAction.class)).collect(Collectors.toList());
				return !l.isEmpty();
			}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			annotatedComponents.forEach((k,v)->{
				SpringAction ann = v.getClass().getAnnotation(SpringAction.class);
				getNamedActions().put(ann.name(), v);
			});
		}
	}
	
}
