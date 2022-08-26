package io.makai.controller;


import io.makai.payload.ApiResponse;
import io.makai.payload.dto.CommentDto;
import io.makai.service.CommentService;
import io.makai.utility.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    private final ErrorMapper errorMapper;

    public CommentController(CommentService commentService, ErrorMapper errorMapper) {
        this.commentService = commentService;
        this.errorMapper = errorMapper;
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable String commentId, @Valid @RequestBody CommentDto commentDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.commentService.update(commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deletePost(@PathVariable String commentId) {
        return this.commentService.delete(commentId);
    }
}