package pe.joseval.bots.dm.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", builderMethodName = "builder")
public class Message {

	private String customMessageName;
	@Default
	private MessageType messageType = MessageType.RAW_TEXT;
	private String messageBody;
	@Singular
	private List<Option> options;
	@Default
	private ActionType actionType = ActionType.NONE;
	private String paramToSet;
	private Map<String, Object> payload;
}
