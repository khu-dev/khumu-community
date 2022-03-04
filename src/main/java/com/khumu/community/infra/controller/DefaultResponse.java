package com.khumu.community.infra.controller;

import lombok.*;
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
    private Links links = new Links();
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Links{
        private String next;
        private String previous;
    }
}
