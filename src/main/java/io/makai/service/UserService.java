package io.makai.service;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.TopUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit);
}
