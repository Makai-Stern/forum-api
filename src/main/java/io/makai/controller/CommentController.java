package io.makai.controller;


import io.makai.payload.ApiResponse;
import io.makai.payload.dto.CommentDto;
import io.makai.payload.dto.ShareDto;
import io.makai.payload.dto.VoteDto;
import io.makai.service.CommentService;
import io.makai.service.ShareService;
import io.makai.service.VoteService;
import io.makai.utility.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    private final ErrorMapper errorMapper;

    private final VoteService voteService;

    private final ShareService shareService;

    public CommentController(CommentService commentService, ErrorMapper errorMapper, VoteService voteService, ShareService shareService) {
        this.commentService = commentService;
        this.errorMapper = errorMapper;
        this.voteService = voteService;
        this.shareService = shareService;
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable String commentId, @Valid @RequestBody CommentDto commentDto, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.commentService.update(commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable String commentId) {
        return this.commentService.delete(commentId);
    }

    @GetMapping("/{commentId}/upvotes")
    public ResponseEntity getCommentUpVotes(@PathVariable String commentId, @RequestParam(name = "pageSize", defaultValue = "0", required = false) int pageSize, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber) {

        VoteDto voteDto = new VoteDto();
        voteDto.setCommentId(commentId);
        voteDto.setIsUpVote(true);
        voteDto.setPageSize(pageSize);
        voteDto.setPageNumber(pageNumber);

        return this.voteService.getVotes(voteDto);
    }

    @GetMapping("/{commentId}/shares")
    public ResponseEntity getCommentShares(@PathVariable String commentId, @RequestParam(name = "pageSize", defaultValue = "0", required = false) int pageSize, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber) {

        ShareDto shareDto = new ShareDto();
        shareDto.setCommentId(commentId);
        shareDto.setPageSize(pageSize);
        shareDto.setPageNumber(pageNumber);

        return this.shareService.getShares(shareDto);
    }

    @GetMapping("/{commentId}/downvotes")
    public ResponseEntity getCommentDownVotes(@PathVariable String commentId, @RequestParam(name = "pageSize", defaultValue = "0", required = false) int pageSize, @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber) {

        VoteDto voteDto = new VoteDto();
        voteDto.setCommentId(commentId);
        voteDto.setIsDownVote(true);
        voteDto.setPageSize(pageSize);
        voteDto.setPageNumber(pageNumber);

        return this.voteService.getVotes(voteDto);
    }
}
