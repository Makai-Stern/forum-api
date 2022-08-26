package io.makai.service.impl;

import io.makai.entity.CommentEntity;
import io.makai.entity.PostEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.EntityNotFoundException;
import io.makai.exception.InvalidPermissionException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.CommentDto;
import io.makai.repository.CommentRepository;
import io.makai.repository.PostRepository;
import io.makai.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ApiResponse<Iterable<CommentEntity>>> getPostComments(String postId, int pageSize, int pageNumber) {

        postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

        if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
            return ResponseEntity.ok(ApiResponse.ok(commentRepository.findByPostId(postId, pageable)));
        }

        return ResponseEntity.ok(ApiResponse.ok(commentRepository.findByPostId(postId)));
    }

    @Override
    public ResponseEntity<ApiResponse<CommentEntity>> create(String postId, CommentDto commentDto) {

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CommentEntity comment = modelMapper.map(commentDto, CommentEntity.class);
        comment.setPost(post);
        comment.setUser(user);

        CommentEntity savedComment = commentRepository.saveAndFlush(comment);

        return ResponseEntity.ok(ApiResponse.ok(savedComment));
    }

    @Override
    public ResponseEntity<ApiResponse<CommentEntity>> update(String commentId, CommentDto commentDto) {

        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!comment.getUser().getId().equalsIgnoreCase(user.getId())) {
            throw new InvalidPermissionException();
        }

        String updatedBody = commentDto.getBody();

        if (comment.getBody().equalsIgnoreCase(updatedBody)) return ResponseEntity.ok(ApiResponse.ok(comment));

        comment.setBody(updatedBody);
        CommentEntity savedComment = commentRepository.saveAndFlush(comment);

        return ResponseEntity.ok(ApiResponse.ok(savedComment));
    }

    @Override
    public ResponseEntity<ApiResponse<CommentEntity>> delete(String commentId) {

        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!comment.getUser().getId().equalsIgnoreCase(user.getId())) {
            throw new InvalidPermissionException();
        }

        commentRepository.delete(comment);
        commentRepository.flush();

        return ResponseEntity.ok(ApiResponse.ok(comment));
    }
}
