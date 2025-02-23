package com.sparta.oishitable.domain.auth.kakao.controller;

import com.sparta.oishitable.domain.auth.dto.response.AuthLoginResponse;
import com.sparta.oishitable.domain.auth.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    /**
     * https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=df3f5d7037ceb763a6a4e826bff0f015&redirect_uri=http://localhost:8080/api/auth/kakao/callback
     * 위의 결과로 code를 받아와서, 해당 코드를 통해 카카오 인증 서버에서 accessToken/refreshToken을 받아옵니다.
     */
    @GetMapping("/callback")
    public ResponseEntity<AuthLoginResponse> getToken(@RequestParam("code") String code) {

        AuthLoginResponse response = kakaoService.kakaoLogin(code);

        return ResponseEntity.ok(response);
    }
}
