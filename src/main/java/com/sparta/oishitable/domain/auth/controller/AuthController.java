package com.sparta.oishitable.domain.auth.controller;

import com.sparta.oishitable.domain.auth.dto.request.AccessTokenReissueReq;
import com.sparta.oishitable.domain.auth.dto.request.AuthSigninRequest;
import com.sparta.oishitable.domain.auth.dto.response.AuthLoginResponse;
import com.sparta.oishitable.domain.auth.dto.response.AuthSignupResponse;
import com.sparta.oishitable.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthSignupResponse> signup(@Valid @RequestBody AuthSigninRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthLoginResponse> signin(@Valid @RequestBody AuthSigninRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> reissueAccessToken(
            @RequestBody AccessTokenReissueReq accessTokenReissueReq
    ) {
        AuthLoginResponse AuthLoginResponse = authService.recreateAccessAndRefreshToken(
                accessTokenReissueReq);
        return ResponseEntity.ok(AuthLoginResponse);
    }
}
