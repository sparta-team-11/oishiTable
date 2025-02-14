package com.sparta.oishitable.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oishitable.global.security.JwtTokenProvider;
import com.sparta.oishitable.global.security.filter.CustomAuthenticationFilter;
import com.sparta.oishitable.global.security.filter.JwtAuthenticationFilter;
import com.sparta.oishitable.global.security.handler.SecurityAccessDeniedHandler;
import com.sparta.oishitable.global.security.handler.SecurityAuthenticationEntryPoint;
import com.sparta.oishitable.global.security.handler.SecurityAuthenticationFailureHandler;
import com.sparta.oishitable.global.security.handler.SecurityAuthenticationSuccessHandler;
import com.sparta.oishitable.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthenticationFilter(), JwtAuthenticationFilter.class)

                .exceptionHandling(handler ->
                        handler.authenticationEntryPoint(new SecurityAuthenticationEntryPoint(objectMapper)))
                .exceptionHandling(handler ->
                        handler.accessDeniedHandler(new SecurityAccessDeniedHandler(objectMapper)))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccessHandler(jwtTokenProvider, objectMapper, redisUtil));
        filter.setAuthenticationFailureHandler(new SecurityAuthenticationFailureHandler(objectMapper));

        filter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository()
                ));

        return filter;
    }
}
