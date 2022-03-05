package com.khumu.community.infra.messaging;

import com.amazonaws.services.sns.AmazonSNS;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class MessagingConfiguration {
	private final AmazonSNS amazonSNS;

	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate() {
		return new NotificationMessagingTemplate(amazonSNS);
	}
}