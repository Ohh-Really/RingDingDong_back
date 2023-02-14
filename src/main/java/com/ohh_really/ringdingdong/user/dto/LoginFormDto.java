package com.ohh_really.ringdingdong.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.repository.query.Param;

@Data
public class LoginFormDto {
    @Schema(description = "이메일", example = "test@gamil.com")
    String email;
    @Schema(description = "구글 고유 식별 값", example = "65168432584651")
    String id;
}
