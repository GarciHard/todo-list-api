package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.repository.UserRepository;
import com.garcihard.todolist.exception.user.UserException;
import com.garcihard.todolist.util.UserConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        List.of() // No roles used by now.
                )).orElseThrow(() -> new BadCredentialsException(UserConstants.INVALID_CREDENTIALS));
    }
}
