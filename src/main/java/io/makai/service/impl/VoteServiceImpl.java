package io.makai.service.impl;

import io.makai.entity.BaseEntity;
import io.makai.entity.CommentEntity;
import io.makai.entity.DownVoteEntity;
import io.makai.entity.PostEntity;
import io.makai.entity.UpVoteEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.ApiException;
import io.makai.exception.EntityNotFoundException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.VoteDto;
import io.makai.repository.CommentRepository;
import io.makai.repository.DownVoteRepository;
import io.makai.repository.PostRepository;
import io.makai.repository.UpVoteRepository;
import io.makai.service.VoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final UpVoteRepository upVoteRepository;

    private final DownVoteRepository downVoteRepository;

    public VoteServiceImpl(PostRepository postRepository, CommentRepository commentRepository, UpVoteRepository upVoteRepository, DownVoteRepository downVoteRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.upVoteRepository = upVoteRepository;
        this.downVoteRepository = downVoteRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> vote(VoteDto voteDto) {

        boolean isUpVote = voteDto.isUpVote();
        boolean isDownVote = voteDto.isDownVote();
        String genericErrorMessage = "Cannot perform that operation";

        if ((isUpVote && isDownVote) || (!isUpVote && !isDownVote)) throw new ApiException(genericErrorMessage);

        if (isUpVote) {
            return upVote(voteDto);
        }

        return downVote(voteDto);
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> getVotes(VoteDto voteDto) {

        boolean isUpVote = voteDto.isUpVote();
        boolean isDownVote = voteDto.isDownVote();
        String genericErrorMessage = "Cannot perform that operation";

        if ((isUpVote && isDownVote) || (!isUpVote && !isDownVote)) throw new ApiException(genericErrorMessage);

        if (isUpVote) {
            return getUpVotes(voteDto);
        }

        return getDownVotes(voteDto);
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> getUpVotes(VoteDto voteDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = voteDto.getPostId();
        String commentId = voteDto.getCommentId();
        if (commentId != null && postId != null) throw new ApiException("Cannot perform that operation");

        int pageSize = voteDto.getPageSize();
        int pageNumber = voteDto.getPageNumber();

        if (postId != null) {

            postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<UpVoteEntity> upVotes = upVoteRepository.findByPostId(postId, pageable);
                apiResponse.setData(upVotes);
            } else {
                List<UpVoteEntity> upVotes = upVoteRepository.findByPostId(postId);
                apiResponse.setData(upVotes);
            }
        }

        if (commentId != null) {

            commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<UpVoteEntity> upVotes = upVoteRepository.findByCommentId(commentId, pageable);
                apiResponse.setData(upVotes);
            } else {
                List<UpVoteEntity> upVotes = upVoteRepository.findByCommentId(commentId);
                apiResponse.setData(upVotes);
            }
        }

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> getDownVotes(VoteDto voteDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = voteDto.getPostId();
        String commentId = voteDto.getCommentId();
        if (commentId != null && postId != null) throw new ApiException("Cannot perform that operation");

        int pageSize = voteDto.getPageSize();
        int pageNumber = voteDto.getPageNumber();

        if (postId != null) {

            postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<DownVoteEntity> downVotes = downVoteRepository.findByPostId(postId, pageable);
                apiResponse.setData(downVotes);
            } else {
                List<DownVoteEntity> upVotes = downVoteRepository.findByPostId(postId);
                apiResponse.setData(upVotes);
            }
        }

        if (commentId != null) {

            commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<DownVoteEntity> downVotes = downVoteRepository.findByCommentId(commentId, pageable);
                apiResponse.setData(downVotes);
            } else {
                List<DownVoteEntity> downVotes = downVoteRepository.findByCommentId(commentId);
                apiResponse.setData(downVotes);
            }
        }

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> upVote(VoteDto voteDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = voteDto.getPostId();
        String commentId = voteDto.getCommentId();

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (postId != null) {
            PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            UpVoteEntity existingUpVote = upVoteRepository.findByUserIdAndPostId(user.getId(), post.getId());
            DownVoteEntity existingDownVote = downVoteRepository.findByUserIdAndPostId(user.getId(), post.getId());

            // Remove Downvote
            if (existingDownVote != null) {
                downVoteRepository.delete(existingDownVote);
            }

            // Check if user already up-voted post
            if (existingUpVote != null) {
                // If user already up-voted the post then remove the upvote
                upVoteRepository.delete(existingUpVote);
                apiResponse.setData("message", "UpVote Removed");
            } else {
                // UpVote the post
                UpVoteEntity upVote = upVoteRepository.saveAndFlush(new UpVoteEntity(post, user));
                apiResponse.setData(upVote);
            }
        }

        if (commentId != null) {
            CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            UpVoteEntity existingUpVote = upVoteRepository.findByUserIdAndCommentId(user.getId(), comment.getId());
            DownVoteEntity existingDownVote = downVoteRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

            // Remove Downvote
            if (existingDownVote != null) {
                downVoteRepository.delete(existingDownVote);
            }

            // Check if user already up-voted post
            if (existingUpVote != null) {
                // If user already up-voted the post then remove the upvote
                upVoteRepository.delete(existingUpVote);
                apiResponse.setData("message", "UpVote Removed");
            } else {
                // UpVote the post
                UpVoteEntity upVote = upVoteRepository.saveAndFlush(new UpVoteEntity(comment, user));
                apiResponse.setData(upVote);
            }

        }

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<BaseEntity>> downVote(VoteDto voteDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = voteDto.getPostId();
        String commentId = voteDto.getCommentId();

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (postId != null) {
            PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            UpVoteEntity existingUpVote = upVoteRepository.findByUserIdAndPostId(user.getId(), post.getId());
            DownVoteEntity existingDownVote = downVoteRepository.findByUserIdAndPostId(user.getId(), post.getId());

            // Remove UpVote
            if (existingUpVote != null) {
                upVoteRepository.delete(existingUpVote);
            }

            // Check if user already up-voted post
            if (existingDownVote != null) {
                // If user already up-voted the post then remove the upvote
                downVoteRepository.delete(existingDownVote);
                apiResponse.setData("message", "DownVote Removed");
            } else {
                // DownVote the post
                DownVoteEntity downVote = downVoteRepository.saveAndFlush(new DownVoteEntity(post, user));
                apiResponse.setData(downVote);
            }
        }

        if (commentId != null) {
            CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            UpVoteEntity existingUpVote = upVoteRepository.findByUserIdAndPostId(user.getId(), comment.getId());
            DownVoteEntity existingDownVote = downVoteRepository.findByUserIdAndPostId(user.getId(), comment.getId());

            // Remove UpVote
            if (existingUpVote != null) {
                upVoteRepository.delete(existingUpVote);
            }

            // Check if user already up-voted post
            if (existingDownVote != null) {
                // If user already up-voted the post then remove the upvote
                downVoteRepository.delete(existingDownVote);
                apiResponse.setData("message", "DownVote Removed");
            } else {
                // DownVote the post
                DownVoteEntity downVote = downVoteRepository.saveAndFlush(new DownVoteEntity(comment, user));
                apiResponse.setData(downVote);
            }
        }

        return ResponseEntity.ok(apiResponse);
    }
}