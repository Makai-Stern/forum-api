package io.makai.service;

import io.makai.entity.PostEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.PostDto;
import io.makai.payload.dto.PostSearchDto;
import org.springframework.http.ResponseEntity;

public interface PostService {

    ResponseEntity<ApiResponse<PostEntity>> create(PostDto postDto);

    ResponseEntity<ApiResponse<Iterable<PostEntity>>> search(PostSearchDto postSearchDto);

    ResponseEntity<ApiResponse<PostEntity>> findById(String postId);

    ResponseEntity<ApiResponse<PostEntity>> delete(String postId);
}
