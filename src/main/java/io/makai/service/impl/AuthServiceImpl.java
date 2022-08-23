package io.makai.service.impl;

import io.makai.constant.AppConstants;
import io.makai.entity.UserEntity;
import io.makai.exception.ApiException;
import io.makai.exception.UnauthorizedException;
import io.makai.payload.ApiResponse;
import io.makai.payload.dto.AuthDto;
import io.makai.repository.RoleRepository;
import io.makai.repository.UserRepository;
import io.makai.service.AuthService;
import io.makai.utility.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import static io.makai.constant.AppConstants.TOKEN_COOKIE_NAME;
import static io.makai.enums.RoleType.ROLE_USER;

@Service
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    public AuthServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public ResponseEntity<ApiResponse> register(AuthDto userData) {

        ApiResponse response = new ApiResponse();

        HttpStatus status;

        try {

            String username = userData.getUsername().toLowerCase();
            String password = bCryptPasswordEncoder.encode(userData.getPassword());

            UserEntity userExists = userRepository.findByUsername(username);
            if (userExists != null) {
                throw new ApiException("Username is taken");
            }

            UserEntity user = new UserEntity(username, password);
            user.setAuthorities(List.of(this.roleRepository.findByName(ROLE_USER.name())));

            response.setData(userRepository.save(user));

            status = HttpStatus.OK;

        } catch (ApiException e) {
            throw new ApiException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return new ResponseEntity<>(response, status);
    }

    @Override
    public ResponseEntity<ApiResponse> login(AuthDto userData, HttpServletResponse httpServletResponse) {

        ApiResponse response = new ApiResponse();

        HttpStatus status;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userData.getUsername(),
                            userData.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = AppConstants.TOKEN_PREFIX +  jwtProvider.generateToken(authentication);

            Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, URLEncoder.encode(token, "UTF-8"));
            cookie.setMaxAge((int) (new Date().getTime() + AppConstants.EXPIRATION_TIME));
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            httpServletResponse.addCookie(cookie);

            //Add token to Api response
            response.setData(TOKEN_COOKIE_NAME, token);

            // Set status
            status = HttpStatus.OK;
        } catch (AuthenticationException e) {
            throw new UnauthorizedException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return new ResponseEntity<>(response, status);
    }


    @Override
    public ResponseEntity<ApiResponse> whoAmI(String token,
                                              HttpServletRequest request) {
        if (token == null) throw new UnauthorizedException();

        ApiResponse apiResponse = new ApiResponse();

        try {
            String userId = jwtProvider.getUserIdFromToken(token.substring(7, token.length()));
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null) throw new UnauthorizedException();
            apiResponse.setData(user);
            apiResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(apiResponse);
    }
}
