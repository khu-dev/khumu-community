package com.khumu.community.infra.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.community.application.port.out.repository.CommentRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class CommentClient implements CommentRepository {
    private final ObjectMapper objectMapper;
    @Value("${khumu.comment.host}")
    private String commentApiHost;

    @Override
    public Long countByArticle(Integer article) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE); // send the post request
        try {
            String payload = objectMapper.writeValueAsString(new CountByArticleRequest(article));
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<CountByArticleResponse> resp = restTemplate.postForEntity(String.format("%s/api/comments/get-comment-count", commentApiHost), entity, CountByArticleResponse.class);
            if (resp.getStatusCode() != HttpStatus.OK) {
                log.error("CommentClient.countByArticle 의 응답이 200이 아닙니다.");
                return 0L;
            }
            if (resp.getBody() == null) {
                log.error("CommentClient.countByArticle 의 응답 body가 null입니다.");
                return 0L;
            }

            return resp.getBody().getCommentCount();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class CountByArticleRequest{
        Integer article;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class CountByArticleResponse{
        Long commentCount;
    }

}
