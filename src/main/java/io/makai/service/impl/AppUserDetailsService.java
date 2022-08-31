package io.makai.service.impl;

import io.makai.entity.UserEntity;
import io.makai.exception.ApiException;
import io.makai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) throw new ApiException("User not found");

        return user;
    }

    public UserDetails loadUserById(String id) {

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ApiException("User not found"));

        return user;
    }
}
