package com.sparta.oishitable.domain.auth.kakao.controller;


import com.sparta.oishitable.domain.auth.kakao.service.KakaoService;
import com.sparta.oishitable.domain.common.auth.dto.response.AuthLoginResponse;
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

    @GetMapping("/callback")
    public ResponseEntity<AuthLoginResponse> getToken(@RequestParam("code") String code) {

        AuthLoginResponse response = kakaoService.kakaoLogin(code);

        return ResponseEntity.ok(response);
    }
}
