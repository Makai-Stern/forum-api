package io.makai.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.makai.config.WebConfiguration;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApiResponse <T> {

    private int status;

    private Map<String, Object> error;

    @JsonIgnore
    private Optional<HttpServletRequest> httpServletRequest = WebConfiguration.getCurrentHttpRequest();

    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date timestamp;

    private T data;

    public ApiResponse() {
        this.timestamp = new Date();
        this.error = new HashMap<>();
        setPath();
    }

    private void setPath() {
        if (httpServletRequest != null) {
            path = httpServletRequest.get().getRequestURI();
        }
    }

    public ApiResponse(HttpStatus status, Map<String, Object> error, T data) {
        this.status = status.value();
        this.error = error;
        this.data = data;
        this.timestamp = new Date();
        setPath();
    }

    public ApiResponse(HttpStatus status, Map<String, Object> error) {
        this.status = status.value();
        this.error = error;
        this.timestamp = new Date();
        setPath();
    }

    public static <T> ApiResponse ok(T data) {
        return new ApiResponse<T>(HttpStatus.OK, null, data);
    }

    public static ApiResponse error(String message, HttpStatus httpStatus) {
        ApiResponse response = new ApiResponse();
        response.setErrorMessage(message);
        response.setStatus(httpStatus);
        return response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Optional<HttpServletRequest> getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(Optional<HttpServletRequest> httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setData(String key, T data) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(key, data);
        this.setData((T) dataMap);
    }

    public void setError(String key, Object error) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put(key, error);
        this.setError(errorMap);
    }

    public void setErrorMessage(String message) {
        this.error.put("message", message);
    }
}
