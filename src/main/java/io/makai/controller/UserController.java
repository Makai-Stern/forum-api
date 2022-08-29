package io.makai.controller;

import io.makai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/top")
    public ResponseEntity getPostComments(@RequestParam(name = "limit", defaultValue = "5", required = false) int limit) {
        return this.userService.getTopUsers(limit);
    }
}
