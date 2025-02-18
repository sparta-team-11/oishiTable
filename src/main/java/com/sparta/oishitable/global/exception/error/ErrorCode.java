package com.sparta.oishitable.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),

    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

    // 식당 관련 익셉션
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 식당 입니다."),

    // SeatType 관련 익셉션
    SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 입니다."),
    SEAT_TYPE_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 이름 입니다."),

    // 팔로우 관련 익셉션
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우 할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 중인 관계입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다."),

    // RESTAURANT_SEAT_TYPE 관련 익셉션
    RESTAURANT_SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 레스토랑 좌석입니다."),
    RESTAURANT_SEAT_TYPE_QUANTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 좌석의 수량이 부족합니다."),
    BELOW_MIN_GUEST_COUNT(HttpStatus.BAD_REQUEST, "좌석 유형에 필요한 최소 인원보다 적습니다."),
    EXCEEDS_MAX_GUEST_COUNT(HttpStatus.BAD_REQUEST, "좌석 유형에 필요한 최대 인원보다 많습니다."),


    // 예약 관련 익셉션
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 입니다."),
    RESERVATION_COUPON_LIMIT_EXCEEDED(HttpStatus.NOT_FOUND, "할인 쿠폰 지급 이벤트가 끝났습니다."),
    RESERVATION_CONFLICT(HttpStatus.NOT_FOUND, "예약 동시성 오류"),

    // Security 관련 익셉션
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NEED_LOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHORIZATION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰 입니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 토큰 입니다."),
    MISSING_CREDENTIALS(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 입력해주세요."),

    // redis
    VALUE_NOT_FOUND_FOR_KEY(HttpStatus.NOT_FOUND, "해당 key에 해당하는 값이 없습니다"),

    // 지역 관련 익셉션
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역 입니다."),

    // 게시글 관련 익셉션
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 입니다."),

    POST_NOT_EQUAL(HttpStatus.BAD_REQUEST, "해당 게시글이 아닙니다."),

    // 댓글 관련 익셉션
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글 입니다."),

    // 좋아요 관련 익셉션
    LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다."),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누르지 않으셨습니다"),

    // 북마크 관련 익셉션
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 북마크입니다."),
    BOOKMARK_ALREADY_EXISTS_RESTAURANT(HttpStatus.CONFLICT, "이미 북마크된 식당입니다."),

    // 컬렉션 관련 익셉션
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 컬렉션입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
