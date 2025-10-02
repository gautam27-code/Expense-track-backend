package com.project.expensepilot.controller;


import com.project.expensepilot.dto.AuthResponseDTO;
import com.project.expensepilot.dto.LoginDto;
import com.project.expensepilot.dto.RegisterDto;
import com.project.expensepilot.model.Role;
import com.project.expensepilot.model.UserEntity;
import com.project.expensepilot.repo.RoleRepo;
import com.project.expensepilot.repo.UserRepo;
import com.project.expensepilot.security.JWTAuthenticationFilter;
import com.project.expensepilot.security.JWTGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo,
                          RoleRepo roleRepo, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepo.findByUsername(loginDto.getUsername()).orElseThrow();
        return new ResponseEntity<>(new AuthResponseDTO(token, user), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto){
        if(userRepo.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username already taken.", HttpStatus.BAD_REQUEST);
        }
        if(userRepo.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is already in use.", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        java.util.Optional<Role> roleOpt = roleRepo.findByName("USER");
        if (roleOpt.isEmpty()) {
            return new ResponseEntity<>("Role USER not found.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setRoles(Collections.singletonList(roleOpt.get()));
        userRepo.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDto.getUsername(),
                        registerDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        UserEntity registeredUser = userRepo.findByUsername(registerDto.getUsername()).orElseThrow();

        return new ResponseEntity<>(new AuthResponseDTO(token, registeredUser), HttpStatus.OK);
    }
}
