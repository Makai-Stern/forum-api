package io.makai.payload.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostDto {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
