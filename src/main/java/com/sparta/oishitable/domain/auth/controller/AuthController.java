package com.sparta.oishitable.domain.auth.controller;

import com.sparta.oishitable.domain.auth.dto.request.AccessTokenReissueReq;
import com.sparta.oishitable.domain.auth.service.AuthService;
import com.sparta.oishitable.global.security.dto.response.AuthLoginResponse;
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> reissueAccessToken(
            @RequestBody AccessTokenReissueReq accessTokenReissueReq
    ) {
        AuthLoginResponse AuthLoginResponse = authService.recreateAccessAndRefreshToken(
                accessTokenReissueReq);
        return ResponseEntity.ok(AuthLoginResponse);
    }
}
