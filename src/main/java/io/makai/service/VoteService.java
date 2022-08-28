package io.makai.service;

import io.makai.entity.BaseEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.VoteDto;
import org.springframework.http.ResponseEntity;

public interface VoteService {

    ResponseEntity<ApiResponse<BaseEntity>> vote(VoteDto voteDto);

    ResponseEntity<ApiResponse<BaseEntity>> getVotes(VoteDto voteDto);

    ResponseEntity<ApiResponse<BaseEntity>> getUpVotes(VoteDto voteDto);

    ResponseEntity<ApiResponse<BaseEntity>> getDownVotes(VoteDto voteDto);

    ResponseEntity<ApiResponse<BaseEntity>> upVote(VoteDto voteDto);

    ResponseEntity<ApiResponse<BaseEntity>> downVote(VoteDto voteDto);
}
