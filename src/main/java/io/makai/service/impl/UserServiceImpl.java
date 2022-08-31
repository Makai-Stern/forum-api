package io.makai.service.impl;

import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.EntityNotFoundException;
import io.makai.exception.InvalidPermissionException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.SearchDto;
import io.makai.payload.dto.TopUserDto;
import io.makai.payload.dto.UserDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public ResponseEntity<ApiResponse<UserEntity>> findById(String userId) {
        return ResponseEntity.ok(ApiResponse.ok(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException())));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TopUserDto>>> getTopUsers(int limit) {
        return ResponseEntity.ok(ApiResponse.ok(userRepository.getTopUsers(limit)));
    }

    @Override
    public ResponseEntity<ApiResponse<Iterable<UserEntity>>> search(SearchDto searchDto) {

        List<UserEntity> users;

        int pageSize = searchDto.getPageSize();
        int pageNumber = searchDto.getPageNumber();

        if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
            Pageable pageable = PageRequest.of(searchDto.getPageNumber(), searchDto.getPageSize(), Sort.by("created_at").descending());
            return ResponseEntity.ok(ApiResponse.ok(userRepository.search(searchDto.getTerm(), pageable)));
        } else {
            users = userRepository.search(searchDto.getTerm());
        }

        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @Override
    public ResponseEntity<ApiResponse<UserEntity>> update(String userId, UserDto userDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity existingUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        if (!user.getId().equalsIgnoreCase(existingUser.getId())) throw new InvalidPermissionException();

        if (userDto.getPassword() != null) {
            String password = bCryptPasswordEncoder.encode(userDto.getPassword());
            existingUser.setPassword(password);
        }

        if (userDto.getUsername() != null) {
            existingUser.setUsername(userDto.getUsername());
        }

        apiResponse.setData(userRepository.save(existingUser));

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<List<PostEntity>>> getPosts(String userId, int pageNumber, int pageSize) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
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
