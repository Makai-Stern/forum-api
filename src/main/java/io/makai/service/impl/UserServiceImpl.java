package io.makai.service.impl;

import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.EntityNotFoundException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.TopUserDto;
import io.makai.repository.PostRepository;
import io.makai.repository.UserRepository;
import io.makai.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit) {
        return ResponseEntity.ok(ApiResponse.ok(userRepository.getTopUsers(limit)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<PostEntity>>> getPosts(String userId, int pageNumber, int pageSize) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
            // Use property "createdAt" because nativeQuery = false
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
            Page<PostEntity> posts = postRepository.findByUserId(userId, pageable);
            apiResponse.setData(posts);
        } else {
            List<PostEntity> posts = postRepository.findByUserId(userId);
            apiResponse.setData(posts);
        }

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<UserEntity>> delete(String userId) {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity savedUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        if (user.getId() == savedUser.getId()) {
            userRepository.delete(savedUser);
        }

        return ResponseEntity.ok(ApiResponse.ok(savedUser));
    }
}
