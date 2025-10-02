package com.project.expensepilot.security;

import com.project.expensepilot.model.Role;
import com.project.expensepilot.model.UserEntity;
import com.project.expensepilot.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private UserRepo userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("CustomUserDetailsService: Looking up user by username: {}", username);
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> {
            logger.error("CustomUserDetailsService: Username not found: {}", username);
            return new UsernameNotFoundException("Username not found");
        });
        logger.info("CustomUserDetailsService: Found user: {} with roles: {}", user.getUsername(), user.getRoles());
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
     }

     private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
     }
}
