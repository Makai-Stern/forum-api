package io.makai.service;

import io.makai.entity.PostEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.PostDto;
import io.makai.payload.dto.PostSearchDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    ResponseEntity<ApiResponse<PostEntity>> create(PostDto postDto);

    ResponseEntity<ApiResponse<Iterable<PostEntity>>> search(PostSearchDto postSearchDto);

    ResponseEntity<ApiResponse<PostEntity>> findById(String postId);

    ResponseEntity<ApiResponse<List<PostEntity>>> findAll(int pageNumber, int pageSize);

    ResponseEntity<ApiResponse<PostEntity>> delete(String postId);
}
