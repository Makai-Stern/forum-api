package io.makai.service.impl;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.TopUserDto;
import io.makai.repository.UserRepository;
import io.makai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit) {
        return ResponseEntity.ok(ApiResponse.ok(userRepository.getTopUsers(limit)));
    }
}
