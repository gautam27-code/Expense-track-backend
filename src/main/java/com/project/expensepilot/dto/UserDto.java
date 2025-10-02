package com.project.expensepilot.dto;

import com.project.expensepilot.model.UserEntity;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String username;
    private String email;

    public UserDto(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}

