package com.khumu.community.application.dto;

// 올바르지 않은, 의도치 못한 DTO가 Event로 message publish되는 것을 막기 위해
// empty interface를 정의함.
public interface BaseEventDto {}
