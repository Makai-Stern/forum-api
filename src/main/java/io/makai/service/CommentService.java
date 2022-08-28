package io.makai.service;

import io.makai.entity.CommentEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.CommentDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<ApiResponse<Iterable<CommentEntity>>> getPostComments(String postId, int pageSize, int pageNumber);

    ResponseEntity<ApiResponse<CommentEntity>> create(String postId, CommentDto commentDto);

    ResponseEntity<ApiResponse<CommentEntity>> update(String commentId, CommentDto commentDto);

    ResponseEntity<ApiResponse<CommentEntity>> delete(String commentId);
}
