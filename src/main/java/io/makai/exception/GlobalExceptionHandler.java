package io.makai.exception;

import io.makai.payload.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleError(ApiException ex, WebRequest request) {

        ApiResponse response = new ApiResponse();
        response.setErrorMessage(ex.getMessage());

        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(statusCode);

        return new ResponseEntity<>(response, statusCode);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleError(UnauthorizedException ex, WebRequest request) {

        ApiResponse response = new ApiResponse();
        response.setErrorMessage(ex.getMessage());

        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
        response.setStatus(statusCode);

        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                 HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse response = new ApiResponse();
        response.setErrorMessage(ex.getMessage());

        HttpStatus statusCode = HttpStatus.NOT_FOUND;
        response.setStatus(statusCode);

        return  new ResponseEntity<>(response, statusCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleError(Exception ex, WebRequest request) {

        ApiResponse response = new ApiResponse();
        response.setErrorMessage("We had trouble processing that request");

        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(statusCode);

        return new ResponseEntity<>(response, statusCode);
    }
}
