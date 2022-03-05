package com.khumu.community.application.port.out.messaging;

import com.khumu.community.application.dto.BaseEventDto;

public interface MessagePublisher {
    void publish(String resourceKind, String eventKind, BaseEventDto payload);
}
