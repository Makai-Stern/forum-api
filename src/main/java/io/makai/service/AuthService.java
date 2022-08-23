package io.makai.service;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.AuthDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    ResponseEntity<ApiResponse> register(AuthDto userData);

    ResponseEntity<ApiResponse> login(AuthDto userData, HttpServletResponse response);

    ResponseEntity<ApiResponse> whoAmI(String token,
                                       HttpServletRequest request);
}
