package com.ohh_really.ringdingdong.pubsub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MessageQueueSchema {

    private String deviceToken;
    private String title;
    private String body;
    private Map<String, String> data;
}
