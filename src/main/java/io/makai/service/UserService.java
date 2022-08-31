package io.makai.service;

import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.SearchDto;
import io.makai.payload.dto.TopUserDto;
import io.makai.payload.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit);

    ResponseEntity<ApiResponse<Iterable<UserEntity>>> search(SearchDto searchDto);

    ResponseEntity<ApiResponse<UserEntity>> update(String userId, UserDto userDto);

    ResponseEntity<ApiResponse<UserEntity>> delete(String userId);

    ResponseEntity<ApiResponse<UserEntity>> findById(String userId);

    ResponseEntity<ApiResponse<List<PostEntity>>> getPosts(String userId, int pageNumber, int pageSize);
}
