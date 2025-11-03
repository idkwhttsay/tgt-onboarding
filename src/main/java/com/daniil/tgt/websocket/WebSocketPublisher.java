package com.daniil.tgt.websocket;

import com.daniil.tgt.dto.SequenceDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPublisher {
    private final SimpMessagingTemplate template;
    public static final String DESTINATION = "/topic/sequences";

    public WebSocketPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(SequenceDto dto) {
        template.convertAndSend(DESTINATION, dto);
    }
}
