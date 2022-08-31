package io.makai.config;

import io.makai.controller.AppAuthenticationEntryPoint;
import io.makai.filter.JwtAuthenticationFilter;
import io.makai.service.impl.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.makai.constant.AppConstants.AUTH_LOGIN_ROUTE;
import static io.makai.constant.AppConstants.AUTH_REGISTER_ROUTE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final AppAuthenticationEntryPoint authenticationEntryPoint;

    private final AppUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfiguration(AppAuthenticationEntryPoint authenticationEntryPoint, AppUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public JwtAuthenticationFilter JwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                .authenticationManager(authenticationManager)
                .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                .antMatchers(AUTH_REGISTER_ROUTE, AUTH_LOGIN_ROUTE).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
