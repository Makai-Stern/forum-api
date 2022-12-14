package io.makai.constant;

public class AppConstants {

    public static final String AUTH_REGISTER_ROUTE = "/api/v1/auth/register";

    public static final String AUTH_LOGIN_ROUTE = "/api/v1/auth/login";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String TOKEN_COOKIE_NAME = "access_token";

    public static final long EXPIRATION_TIME = 86_400_000; // 1 day in milliseconds
}
