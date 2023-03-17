package com.ohh_really.ringdingdong.fcm.controller;

import com.ohh_really.ringdingdong.fcm.dto.FcmMessage;
import com.ohh_really.ringdingdong.fcm.dto.RegionMessageDto;
import com.ohh_really.ringdingdong.fcm.service.FcmMessingService;
import com.ohh_really.ringdingdong.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class FcmController {

    FcmService fcmService;
    FcmMessingService fcmMessingService;

    @Autowired
    public FcmController(FcmService fcmService, FcmMessingService fcmMessingService) {
        this.fcmService = fcmService;
        this.fcmMessingService = fcmMessingService;
    }

    @PostMapping("/fcm")
    @Deprecated
    @Operation(summary = "Send FCM Message")
    public ResponseEntity<String> pushMessage(
            @RequestBody FcmMessage fcmMessage) throws Exception {
        fcmService.sendMessageTo(
                fcmMessage.getMessage().getToken(),
                fcmMessage.getMessage().getNotification().getTitle(),
                fcmMessage.getMessage().getNotification().getBody()
        );
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/fcm/country")
    @Operation(summary = "국가별 FCM 전송, Token은 Null 로 보내주세요")
    public ResponseEntity<String> pushMessageByCountry(
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token,
            @RequestBody RegionMessageDto regionMessageDto
    ) throws Exception {
        return fcmMessingService.sendMessageToCountry(regionMessageDto, token);
    }
}

