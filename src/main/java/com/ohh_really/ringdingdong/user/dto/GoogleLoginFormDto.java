package com.ohh_really.ringdingdong.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginFormDto {
    @Schema(description = "별명", example = "Test User")
    String displayName;
    @Schema(description = "이메일", example = "test@gamil.com")
    String email;
    @Schema(description = "Google 고유값", example = "165165168468168435180")
    String id;
    @Schema(description = "Google 프로필 사진", example = "https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
    String photoUrl;
}
