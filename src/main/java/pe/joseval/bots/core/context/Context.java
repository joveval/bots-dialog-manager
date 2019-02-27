package pe.joseval.bots.core.context;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Context {
	private String id;
	private String sessionId;
	Map<String, Object> mapVariables;
}
