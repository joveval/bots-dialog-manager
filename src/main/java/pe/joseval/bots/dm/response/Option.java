package pe.joseval.bots.dm.response;

import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder(builderClassName="Builder",builderMethodName="builder")
public class Option {
	
	private OptionType optionType;
	@Default
	private ActionType actionType=ActionType.NONE;
	private String icon;
	private String text;
	private String template;
	private String htmlTemplate;
	private String externalLink;
	private Map<String, Object> payload;
}
