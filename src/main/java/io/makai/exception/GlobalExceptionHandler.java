package io.makai.exception;

import io.makai.payload.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<ApiResponse> exceptionMapper(String message, HttpStatus statusCode) {

        ApiResponse response = new ApiResponse();
        response.setErrorMessage(message);
        response.setStatus(statusCode);

        return new ResponseEntity<>(response, statusCode);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleError(ApiException ex) {
        return exceptionMapper(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleError(UnauthorizedException ex) {
        return exceptionMapper(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPermissionException.class)
    public ResponseEntity<ApiResponse> handleError(InvalidPermissionException ex) {
        return exceptionMapper(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleError(EntityNotFoundException ex) {
        return exceptionMapper(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                 HttpHeaders headers, HttpStatus status, WebRequest request) {
        return exceptionMapper(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleError(Exception ex) {
        return exceptionMapper("We had trouble processing that request", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
