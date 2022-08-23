package io.makai.payload.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO for Auth requests
 * Used one DTO for Register and Login since the same fields are required for both requests
 */
public class AuthDto {

    @NotBlank
    @Size(min=3, max=12)
    private String username;

    @NotBlank
    @Size(min=6, max=24)
    private String password;

    public AuthDto() {}

    public AuthDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
