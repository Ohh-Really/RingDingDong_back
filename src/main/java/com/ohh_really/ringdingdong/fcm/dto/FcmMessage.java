package com.ohh_really.ringdingdong.fcm.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
