package io.makai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.makai.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AppAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles bad auth request
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

        ApiResponse apiResponse = new ApiResponse(statusCode, Map.of("message", "Unauthorized"), null);

        String writerStr;
        try {
            writerStr = objectMapper.writeValueAsString(apiResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            writerStr = "Not authenticated";
        }

        response.setContentType("application/json");
        response.setStatus(statusCode.value());
        response.getWriter().print(writerStr);
    }
}