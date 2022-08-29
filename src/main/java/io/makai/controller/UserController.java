package io.makai.controller;

import io.makai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity getTopUsers(@RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return this.userService.getTopUsers(pageSize);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserPosts(@PathVariable String userId, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return this.userService.getPosts(userId, pageNumber, pageSize);
    }

    @GetMapping("/{userId}/feed")
    public ResponseEntity getUserFeed(@PathVariable String userId, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return this.userService.getPosts(userId, pageNumber, pageSize);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        return this.userService.delete(userId);
    }
}
