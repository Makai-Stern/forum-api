package io.makai.controller;

import io.makai.entity.PostEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.PostDto;
import io.makai.payload.dto.PostSearchDto;
import io.makai.service.PostService;
import io.makai.utility.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    private final ErrorMapper errorMapper;

    public PostController(PostService postService, ErrorMapper errorMapper) {
        this.postService = postService;
        this.errorMapper = errorMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@Valid @RequestBody PostDto postDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.postService.create(postDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostEntity>> getPost(@PathVariable String postId) {
        return this.postService.findById(postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostEntity>> deletePost(@PathVariable String postId) {
        return this.postService.delete(postId);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends ApiResponse> search(@Valid @RequestBody PostSearchDto postSearchDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.postService.search(postSearchDto);
    }
}
