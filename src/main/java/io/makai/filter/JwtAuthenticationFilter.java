package io.makai.filter;

import io.makai.exception.UnauthorizedException;
import io.makai.service.impl.AppUserDetailsService;
import io.makai.utility.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static io.makai.constant.AppConstants.TOKEN_COOKIE_NAME;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AppUserDetailsService userDetailsService;

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie cookie = WebUtils.getCookie(request, TOKEN_COOKIE_NAME);
        if (cookie != null) {
            try {

                String token = jwtProvider.getTokenFromRequest(request);

                if (StringUtils.hasText(token) && jwtProvider.isValidToken(token)) {

                    String userId = jwtProvider.getUserIdFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, Collections.emptyList());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    removeTokenCookie(response);
                }
            } catch (Exception e) {
                removeTokenCookie(response);
                throw new UnauthorizedException();
            }
        }
        filterChain.doFilter(request, response);
    }

    private void removeTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //add a cookie to response
        response.addCookie(cookie);
    }
}
