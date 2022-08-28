package io.makai.service;

import io.makai.entity.ShareEntity;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.ShareDto;
import org.springframework.http.ResponseEntity;

public interface ShareService {

    ResponseEntity<ApiResponse<ShareEntity>> share(ShareDto shareDto);

    ResponseEntity<ApiResponse<Iterable<ShareEntity>>> getShares(ShareDto shareDto);
}
