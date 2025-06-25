package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.repository.UserRepository;
import com.garcihard.todolist.security.CustomUserDetails;
import com.garcihard.todolist.util.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new CustomUserDetails(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        List.of() // No roles used by now.
                )).orElseThrow(() -> new BadCredentialsException(ApiConstants.INVALID_CREDENTIALS));
    }
}
