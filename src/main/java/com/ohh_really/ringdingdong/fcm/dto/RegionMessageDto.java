package com.ohh_really.ringdingdong.fcm.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegionMessageDto {
    private String title;
    private String body;
    private String region;
}
