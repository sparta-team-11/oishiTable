package com.sparta.oishitable.global.security.filter;

import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.global.security.JwtTokenProvider;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveHeader(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateAccessToken(token)) {
            User user = jwtTokenProvider.getUserFromToken(token);
            CustomUserDetails userDetails = new CustomUserDetails(user);

            Authentication authToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token == null || !jwtTokenProvider.isStartsWithBearer(token)) {
            return null;
        }

        return jwtTokenProvider.removeBearer(token);
    }
}
