package com.ohh_really.ringdingdong.pubsub.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ohh_really.ringdingdong.pubsub.dto.MessageContent;
import com.ohh_really.ringdingdong.pubsub.dto.MessageQueueSchema;
import com.ohh_really.ringdingdong.pubsub.dto.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class PubSubController {

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.gcp.topic-name}")
    private String topicUrl;

    @PostMapping("/notification/{topic}")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @PathVariable String topic,
            @RequestBody MessageContent content
    ) throws JsonProcessingException {
        MessageQueueSchema queueSchema = new MessageQueueSchema(
                content.getDeviceToken(),
                content.getTitle(),
                content.getBody(),
                content.getData()
        );
        String message = objectMapper.writeValueAsString(queueSchema);
        log.info("message: " + message);

        this.pubSubTemplate.publish(topicUrl+topic, message);

        return ResponseEntity.ok(new SendMessageResponse(true));
    }
}
