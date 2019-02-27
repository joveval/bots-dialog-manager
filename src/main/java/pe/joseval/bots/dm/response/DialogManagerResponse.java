package pe.joseval.bots.dm.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "messages","responseType","payload","sessionId" })
@Builder(builderClassName="Builder",buildMethodName="build")
public class DialogManagerResponse {

	@Singular
	private List<Message> messages;
	private ResponseType responseType;
	private Map<String, Object> payload;
	private String paramToFill;
	private String sessionId;

	public static class DatabaseErrorMessage {
		public static DialogManagerResponse build() {
			return new DialogManagerResponse.Builder()
					.message(new Message.Builder()
										  .messageBody("No he podido obtener datos de mi fuente de datos. Intenta de nuevo más tarde.")
										  .build())
					.responseType(ResponseType.FINAL).build();
		}

	}

	public static class GeneralErrorMessage {
		public static DialogManagerResponse build() {
			return new DialogManagerResponse.Builder()
					//.singleMsg("Algo sucedio en mis neuronas por favor intenta decirme de nuevo las cosas.")
					.message(new Message.Builder()
										  .messageBody("Algo sucedio en mis neuronas por favor intenta decirme de nuevo las cosas.")
										  .build())
					.responseType(ResponseType.FINAL).build();
		}
	}

}
