package com.project.expensepilot.controller;


import com.project.expensepilot.dto.RegisterDto;
import com.project.expensepilot.model.Role;
import com.project.expensepilot.model.UserEntity;
import com.project.expensepilot.repo.RoleRepo;
import com.project.expensepilot.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo,
                          RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        if(userRepo.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username already taken.", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepo.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));
        userRepo.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
