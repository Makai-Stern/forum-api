package io.makai.utility;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.makai.constant.AppConstants;
import io.makai.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.makai.constant.AppConstants.TOKEN_COOKIE_NAME;
import static io.makai.constant.AppConstants.TOKEN_PREFIX;

@Component
public class JwtProvider {

    @Autowired
    private Dotenv dotenv;

    public String generateToken(Authentication authentication) {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + AppConstants.EXPIRATION_TIME);

        String userId = user.getId();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, dotenv.get("SECRET_KEY"))
                .compact();
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parser().setSigningKey(dotenv.get("SECRET_KEY")).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {

        Claims claims = Jwts.parser().setSigningKey(dotenv.get("SECRET_KEY")).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        return id;
    }

    /**
     * Gets Token from request cookies
     *
     * @param request HttpServletRequest
     * @return cookie/null
     */
    public String getTokenFromRequest(HttpServletRequest request) {

        Cookie cookie = WebUtils.getCookie(request, TOKEN_COOKIE_NAME);
        String token = cookie.getValue().replace("Bearer+", TOKEN_PREFIX);

        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }

        return null;
    }
}
