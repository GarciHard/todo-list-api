package com.garcihard.todolist.service;

import com.garcihard.todolist.model.dto.LoginRequestDTO;
import com.garcihard.todolist.model.dto.LoginResponseDTO;
import com.garcihard.todolist.security.CustomUserDetails;
import com.garcihard.todolist.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO authRequest) {
        String jwt = getJwt(authRequest);
        Long expiration = jwtUtil.extractExpirationMillis(jwt);

        return LoginResponseDTO.of(expiration, jwt);
    }

    private String getJwt(LoginRequestDTO authRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }
}
