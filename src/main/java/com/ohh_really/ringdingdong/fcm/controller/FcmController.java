package com.ohh_really.ringdingdong.fcm.controller;

import com.ohh_really.ringdingdong.fcm.dto.FcmMessage;
import com.ohh_really.ringdingdong.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FcmController {

    FcmService fcmService;

    @Autowired
    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/fcm")
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
}

