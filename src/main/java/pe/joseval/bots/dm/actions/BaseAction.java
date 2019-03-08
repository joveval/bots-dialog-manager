package pe.joseval.bots.dm.actions;

import java.util.Map;

public interface BaseAction<T> {
	T execute(Map<String, Object> params);
}
