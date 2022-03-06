package com.khumu.community.infra.controller;

import lombok.*;
import org.springframework.data.domain.Page;
import org.w3c.dom.stylesheets.LinkStyle;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DefaultResponse<T> {
    private String message;
    private T data;
    private String error;
//    private Links links = new Links();

//    @NoArgsConstructor
//    @Getter
//    @Setter
//    public static class Links{
//        private String next;
//        private String previous;
//
//        public Links(Page<Object> page, )
//    }
}
