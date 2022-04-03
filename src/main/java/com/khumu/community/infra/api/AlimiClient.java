package com.khumu.community.infra.api;

import com.khumu.community.application.port.out.repository.AlimiRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
@Slf4j
public class AlimiClient implements AlimiRepository {
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Value("${khumu.alimi.host}")
    private String alimiApiHost;

    @Override
    public Boolean isSubscribed(String username, String resourceKind, String resourceId) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(mappingJackson2HttpMessageConverter)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // send the post request
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<AlimiClient.IsSubscribedHttpResponse> resp = restTemplate.exchange(
                String.format("%s/api/subscriptions/%s/%s/%s", alimiApiHost, username, resourceKind, resourceId),
                HttpMethod.GET,
                entity,
                AlimiClient.IsSubscribedHttpResponse.class
        );

        if (resp.getStatusCode() != HttpStatus.OK) {
            log.error("AlimiClient.isSubscribed 의 응답이 200이 아닙니다.");
            return false;
        }
        if (resp.getBody() == null) {
            log.error("AlimiClient.isSubscribed 의 응답 body가 null입니다.");
            return false;
        }

        log.info("AlimiClient.isSubscribed 의 응답: " + resp.getBody());

        return resp.getBody().getData().getIsActivated();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class IsSubscribedHttpResponse{
        private IsSubscribedResponse data;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        @ToString
        private static class IsSubscribedResponse{
            private Long resourceId;
            private String resourceKind;
            private String subscriber;
            private Boolean isActivated;
        }

    }
}
