package io.makai.utility;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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
        } catch (SignatureException e) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public String getUserIdFromToken(String token){

        Claims claims = Jwts.parser().setSigningKey(dotenv.get("SECRET_KEY")).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        return id;
    }

    /**
     * Gets Token from request cookies
     * @param request HttpServletRequest
     * @return cookie/null
     */
    public String getTokenFromRequest(HttpServletRequest request){

        Cookie cookie = WebUtils.getCookie(request, "access_token");
        String token = cookie.getValue().replace("Bearer+", "Bearer ");

        if(StringUtils.hasText(token) && token.startsWith(AppConstants.TOKEN_PREFIX)){
            return token.substring(7, token.length());
        }

        return null;
    }
}
