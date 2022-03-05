package com.khumu.community.infra.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.community.application.dto.BaseEventDto;
import com.khumu.community.application.port.out.messaging.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SnsPublisher implements MessagePublisher {
	private final NotificationMessagingTemplate notificationMessagingTemplate;
	private final ObjectMapper jacksonObjectMapper;
	@Value("${khumu.messaging.sns.topic}")
	private String snsTopic;

//	public void setSnsTopic(String topic) {
//		this.snsTopic = topic;
//	}
	@Override
	public void publish(String resourceKind, String eventKind, BaseEventDto payload) {
		Map<String, Object> m = new HashMap<>();
		m.put("resource_kind", resourceKind);
		m.put("event_kind", eventKind);

		try {
			String payloadString = jacksonObjectMapper.writeValueAsString(payload);
			this.notificationMessagingTemplate.convertAndSend(snsTopic, payloadString, m);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}