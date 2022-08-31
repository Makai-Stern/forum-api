package io.makai.controller;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.SearchDto;
import io.makai.payload.dto.UserDto;
import io.makai.service.UserService;
import io.makai.utility.ErrorMapper;
import io.makai.validator.UserDtoValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final ErrorMapper errorMapper;

    private final UserDtoValidator userDtoValidator;

    public UserController(UserService userService, ErrorMapper errorMapper, UserDtoValidator userDtoValidator) {
        this.userService = userService;
        this.errorMapper = errorMapper;
        this.userDtoValidator = userDtoValidator;
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable String userId) {
        return this.userService.findById(userId);
    }

    @GetMapping("/top")
    public ResponseEntity getTopUsers(@RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return this.userService.getTopUsers(pageSize);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity getUserPosts(@PathVariable String userId, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return this.userService.getPosts(userId, pageNumber, pageSize);
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto, BindingResult result) {

        userDtoValidator.validate(userDto, result);
        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        return this.userService.delete(userId);
    }

    @PostMapping("/search")
    public ResponseEntity search(@Valid @RequestBody SearchDto searchDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.userService.search(searchDto);
    }
}
