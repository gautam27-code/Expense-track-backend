package com.project.expensepilot.dto;

import com.project.expensepilot.model.UserEntity;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private  String accessToken;
    private String tokenType = "Bearer ";
    private UserDto user;

    public AuthResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }

    public AuthResponseDTO(String accessToken, UserEntity user) {
        this.accessToken = accessToken;
        this.user = new UserDto(user);
    }
}