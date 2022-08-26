package io.makai.payload.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentDto {

    @NotBlank
    @Size(max = 255)
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
