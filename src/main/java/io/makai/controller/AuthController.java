package io.makai.controller;

import io.makai.payload.ApiResponse;
import io.makai.payload.dto.AuthDto;
import io.makai.service.AuthService;
import io.makai.utility.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final ErrorMapper errorMapper;

    public AuthController(
            AuthService authService,
            ErrorMapper errorMapper) {
        this.authService = authService;
        this.errorMapper = errorMapper;
    }

    @GetMapping("/whoami")
    public ResponseEntity<ApiResponse> whoAmI(
            @CookieValue(name = "access_token", required = false) String token,
            HttpServletRequest request) {

        return this.authService.whoAmI(token, request);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody AuthDto userData, BindingResult result) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.authService.register(userData);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody AuthDto userData,
            BindingResult result,
            HttpServletResponse response) {

        ResponseEntity<ApiResponse> errorMap = errorMapper.validate(result);
        if (errorMap != null) return errorMap;

        return this.authService.login(userData, response);
    }
}
