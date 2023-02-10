package com.ohh_really.ringdingdong.user.dto;

import lombok.Data;

@Data
public class UserRegisterForm {
    private String userId;
    private String username;
    private String password;
    private String passwordConfirm;
}
