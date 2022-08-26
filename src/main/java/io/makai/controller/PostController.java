package io.makai.controller;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.CommentDto;
import io.makai.payload.dto.PostDto;
import io.makai.payload.dto.PostSearchDto;
import io.makai.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    private final ErrorMapper errorMapper;

    public PostController(PostService postService, CommentService commentService, ErrorMapper errorMapper) {
        this.postService = postService;
        this.commentService = commentService;
        this.errorMapper = errorMapper;
    }

    @PostMapping
    public ResponseEntity createPost(@Valid @RequestBody PostDto postDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.postService.create(postDto);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity getPostComments(@PathVariable String postId, @Valid @RequestBody CommentDto commentDto, BindingResult result)  {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.commentService.create(postId, commentDto);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity getPostComments(@PathVariable String postId, @RequestParam(name = "pageSize", defaultValue = "0", required = false) int pageSize, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber) {
        return this.commentService.getPostComments(postId, pageSize, pageNumber);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable String postId) {
        return this.postService.delete(postId);
    }

    @PostMapping("/search")
    public ResponseEntity search(@Valid @RequestBody PostSearchDto postSearchDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.postService.search(postSearchDto);
    }
}
