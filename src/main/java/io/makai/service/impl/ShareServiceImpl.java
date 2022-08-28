package io.makai.service.impl;

import io.makai.entity.CommentEntity;
import io.makai.entity.PostEntity;
import io.makai.entity.ShareEntity;
import io.makai.entity.UserEntity;
import io.makai.exception.ApiException;
import io.makai.exception.EntityNotFoundException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.ShareDto;
import io.makai.repository.CommentRepository;
import io.makai.repository.PostRepository;
import io.makai.repository.ShareRepository;
import io.makai.service.ShareService;
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
public class ShareServiceImpl implements ShareService {

    private final ShareRepository shareRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public ShareServiceImpl(ShareRepository shareRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.shareRepository = shareRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ResponseEntity<ApiResponse<ShareEntity>> share(ShareDto shareDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = shareDto.getPostId();
        String commentId = shareDto.getCommentId();

        if (commentId != null && postId != null) throw new ApiException("Cannot perform that operation");

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (postId != null) {
            PostEntity post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            ShareEntity existingShare = shareRepository.findByUserIdAndPostId(user.getId(), post.getId());

            // Check if user already up-voted post
            if (existingShare != null) {
                // If user already up-voted the post then remove the upvote
                shareRepository.delete(existingShare);
                apiResponse.setData("message", "Share Removed");
            } else {
                // UpVote the post
                ShareEntity share = shareRepository.saveAndFlush(new ShareEntity(post, user));
                apiResponse.setData(share);
            }
        }

        if (commentId != null) {
            CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            ShareEntity existingShare = shareRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

            // Check if user already up-voted post
            if (existingShare != null) {
                // If user already up-voted the post then remove the upvote
                shareRepository.delete(existingShare);
                apiResponse.setData("message", "Share Removed");
            } else {
                // UpVote the post
                ShareEntity share = shareRepository.saveAndFlush(new ShareEntity(comment, user));
                apiResponse.setData(share);
            }
        }

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Iterable<ShareEntity>>> getShares(ShareDto shareDto) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK);

        String postId = shareDto.getPostId();
        String commentId = shareDto.getCommentId();
        if (commentId != null && postId != null) throw new ApiException("Cannot perform that operation");

        int pageSize = shareDto.getPageSize();
        int pageNumber = shareDto.getPageNumber();

        if (postId != null) {

            postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<ShareEntity> posts = shareRepository.findSharedPostsByPostId(postId, pageable);
                apiResponse.setData(posts);
            } else {
                List<ShareEntity> posts = shareRepository.findSharedPostsByPostId(postId);
                apiResponse.setData(posts);
            }
        }

        if (commentId != null) {

            commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

            if (pageSize > 0 && (pageNumber == 0 || pageNumber > 0)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());
                Page<ShareEntity> comments = shareRepository.findSharedCommentsByCommentId(commentId, pageable);
                apiResponse.setData(comments);
            } else {
                List<ShareEntity> comments = shareRepository.findSharedCommentsByCommentId(commentId);
                apiResponse.setData(comments);
            }
        }

        return ResponseEntity.ok(apiResponse);
    }
}
