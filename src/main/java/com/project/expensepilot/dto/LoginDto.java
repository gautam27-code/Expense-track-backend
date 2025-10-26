package com.project.expensepilot.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank(message = "Username is required")
    private String username;

    //

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
