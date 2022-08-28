package io.makai.service.impl;

import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.EntityNotFoundException;
import io.makai.exception.InvalidPermissionException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.PostDto;
import io.makai.payload.dto.PostSearchDto;
import io.makai.repository.PostRepository;
import io.makai.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ApiResponse<PostEntity>> create(PostDto postDto) {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostEntity post = modelMapper.map(postDto, PostEntity.class);
        post.setUser(user);
        PostEntity savedPost = postRepository.saveAndFlush(post);

        return ResponseEntity.ok(ApiResponse.ok(savedPost));
    }

    @Override
    public ResponseEntity<ApiResponse<Iterable<PostEntity>>> search(PostSearchDto postSearchDto) {

        List<PostEntity> posts;

        int pageSize = postSearchDto.getPageSize();
        int pageNumber = postSearchDto.getPageNumber();

        if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
            Pageable pageable = PageRequest.of(postSearchDto.getPageNumber(), postSearchDto.getPageSize(), Sort.by("created_at").descending());
            return ResponseEntity.ok(ApiResponse.ok(postRepository.search(postSearchDto.getTerm(), pageable)));
        } else {
            posts = postRepository.search(postSearchDto.getTerm());
        }

        return ResponseEntity.ok(ApiResponse.ok(posts));
    }

    @Override
    public ResponseEntity<ApiResponse<PostEntity>> findById(String postId) {

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

        return ResponseEntity.ok(ApiResponse.ok(post));
    }

    @Override
    public ResponseEntity<ApiResponse<PostEntity>> delete(String postId) {

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!post.getUser().getId().equalsIgnoreCase(user.getId())) {
            throw new InvalidPermissionException();
        }

        postRepository.delete(post);
        postRepository.flush();

        return ResponseEntity.ok(ApiResponse.ok(post));
    }
}
