package io.makai.utility;

import io.makai.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorMapper {

    public ResponseEntity<ApiResponse> validate(BindingResult result) {

        if (result.hasErrors()) {
            Map<String, Object> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            HttpStatus statusCode = HttpStatus.BAD_REQUEST;

            ApiResponse response = new ApiResponse(statusCode, errorMap);

            return new ResponseEntity<ApiResponse>(response, statusCode);
        }

        return null;

    }
}