package io.makai.service;

import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.TopUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit);

    ResponseEntity<ApiResponse<UserEntity>> delete(String userId);

    ResponseEntity<ApiResponse<List<PostEntity>>> getPosts(String userId, int pageNumber, int pageSize);
}
