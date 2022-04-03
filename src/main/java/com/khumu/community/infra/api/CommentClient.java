package com.khumu.community.infra.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.CommentRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class CommentClient implements CommentRepository {
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Value("${khumu.comment.host}")
    private String commentApiHost;

    @Override
    public Long countByArticle(Integer article) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(mappingJackson2HttpMessageConverter)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // send the post request
        HttpEntity<CountByArticleRequest> entity = new HttpEntity<>(new CountByArticleRequest(article), headers);
        ResponseEntity<CountByArticleResponse> resp = restTemplate.postForEntity(String.format("%s/api/comments/get-comment-count", commentApiHost), entity, CountByArticleResponse.class);
        if (resp.getStatusCode() != HttpStatus.OK) {
            log.error("CommentClient.countByArticle 의 응답이 200이 아닙니다.");
            return 0L;
        }
        if (resp.getBody() == null) {
            log.error("CommentClient.countByArticle 의 응답 body가 null입니다.");
            return 0L;
        }

        log.info("CommentClient.countByArticle 의 응답: " + resp.getBody());

        return resp.getBody().getCommentCount();
    }

    @Override
    public List<Integer> findAllArticlesUserCommented(String authorizationString, Pageable pageable) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE); // send the post request
        headers.set("Authorization", authorizationString);
        // Spring의 Pageable은 Page Number을 0부터 세지만, comment는 1부터 센다.
        HttpEntity<FindAllArticlesUserCommentedRequest> entity = new HttpEntity<>(new FindAllArticlesUserCommentedRequest(pageable.getPageNumber() + 1, pageable.getPageSize()), headers);
        ResponseEntity<FindAllArticlesUserCommentedResponse> resp = restTemplate.postForEntity(String.format("%s/api/comments/get-commented-articles", commentApiHost), entity, FindAllArticlesUserCommentedResponse.class);
        if (resp.getStatusCode() != HttpStatus.OK) {
            log.error("CommentClient.findAllArticlesUserCommented 의 응답이 200이 아닙니다.");
            return new ArrayList<>();
        }
        if (resp.getBody() == null) {
            log.error("CommentClient.findAllArticlesUserCommented 의 응답 body가 null입니다.");
            return new ArrayList<>();
        }

        log.info("CommentClient.findAllArticlesUserCommented 의 응답: " + resp.getBody());

        return resp.getBody().getData();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class CountByArticleRequest{
        Integer article;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class CountByArticleResponse{
        Long commentCount = 0L;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class FindAllArticlesUserCommentedRequest{
        Integer page;
        Integer size;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class FindAllArticlesUserCommentedResponse{
        List<Integer> data = new ArrayList<>();
        String message;
    }
}
