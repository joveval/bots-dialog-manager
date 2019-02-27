package pe.joseval.bots.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.joseval.bots.dm.response.DialogManagerResponse;
import pe.joseval.bots.dm.response.DialogManagerResponse.GeneralErrorMessage;
import pe.joseval.bots.dm.response.Message;
import pe.joseval.bots.dm.response.ResponseType;

@Slf4j
@NoArgsConstructor
public class DefaultActions {

	public DialogManagerResponse getSimpleMultilineMessage(String[] messages) {

		return getSimpleMultilineMessage(messages, null);
	}

	public DialogManagerResponse getSimpleMultilineMessage(String[] messages, SimpleMessageCustomizer customizer) {

		DialogManagerResponse response = null;
		List<String> multiLineMsg = Arrays.asList(messages);
		List<Message> messageList = new ArrayList<>();

		multiLineMsg.forEach(a -> {

			messageList.add(Message.builder().messageBody(a).build());
		});

		try {
			if (customizer != null) {

				response = DialogManagerResponse.builder().messages(messageList)
						.responseType(customizer.getResponseType()).paramToFill(customizer.getParamToFill()).build();
			} else {

				response = DialogManagerResponse.builder().messages(messageList).build();
			}

			log.debug("[RESPONSE]: {}", response);
		} catch (Exception e) {

			log.warn("[ERROR][getSimpleMultilineMessage]:", e);
			return GeneralErrorMessage.build();
		}
		return response;
	}

	public DialogManagerResponse getPlainResponseFromList(String[] messages, SimpleMessageCustomizer customizer) {
		DialogManagerResponse response = null;

		try {

			if (customizer != null) {

				response = DialogManagerResponse.builder()
						.message(Message.builder().messageBody(getRandomItem(messages)).build())
						.responseType(customizer.getResponseType()).paramToFill(customizer.getParamToFill()).build();

			} else {
				response = DialogManagerResponse.builder()
						.message(Message.builder().messageBody(getRandomItem(messages)).build())
						.responseType(ResponseType.FINAL).build();
			}

			log.debug("[RESPONSE]: {}", response);
		} catch (Exception e) {

			log.warn("[ERROR][getPlainResponseFromList	]:", e);
			return GeneralErrorMessage.build();
		}
		return response;
	}

	public DialogManagerResponse getPlainResponseFromList(String[] list) {

		return getPlainResponseFromList(list, null);
	}

	public static <T> T getRandomItem(T[] array) {
		if (array == null)
			return null;
		int rndIndex = ThreadLocalRandom.current().nextInt(0, array.length);
		T rndItem = array[rndIndex];
		return rndItem;
	}
}
