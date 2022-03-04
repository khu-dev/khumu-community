package com.khumu.community.infra.slack;

import javax.annotation.PostConstruct;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.community.application.port.out.noti.Notifier;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackNotifier implements Notifier {
    @Value("${notification.slack.enabled}")
    private boolean slackEnabled;
    @Value("${notification.slack.webhook.url}")
    private String webhookUrl;
    @Value("${notification.slack.channel}")
    private String channel;
    @Value("${notification.slack.icon.emoji}") private String iconEmoji;

    @Autowired
    Environment env;

    private final ObjectMapper jacksonObjectMapper;

    @PostConstruct
    public void postConstructor(){
//        slack = Slack.getInstance();
    }
    public void notify(String title, String content) {
        if (slackEnabled) {
            try { // create slack
                SlackMessage slackMessage = SlackMessage.builder().attachments(List.of(SlackMessage.Attachment.builder()
                        .title(title)
                        .text(content)
                        .footer("from " + env.getActiveProfiles()[0])
                        .build()
                ))
                        .iconEmoji(iconEmoji)
                        .username("신고관리자")
                        .channel(channel).build();
                String payload = jacksonObjectMapper.writeValueAsString(slackMessage);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE); // send the post request
                HttpEntity<String> entity = new HttpEntity<>(payload, headers);
                restTemplate.postForEntity(webhookUrl, entity, String.class);
            } catch (Exception e) {
                log.error("Unhandled Exception occurred while send slack. [Reason] : ", e);
            }
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SlackMessage {
        private String text;
        private String channel;
        private String username;
        private String iconEmoji;
        private String iconUrl;
        private List<Attachment> attachments;

        @Builder
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Attachment {
            private String title;
            private String text;
            @Builder.Default
            private String color = "#BB2222";
            private String footer;
        }
    }
}

