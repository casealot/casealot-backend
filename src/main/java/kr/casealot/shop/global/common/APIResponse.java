package kr.casealot.shop.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class APIResponse<T> {

    private final static int SUCCESS = 200;
    private final static int CREATE = 201;
    private final static int NO_CONTENT = 204;
    private final static int BAD_REQUEST = 400;
    private final static int NOT_FOUND = 404;
    private final static int CONFLICT = 409;
    private final static int FAILED = 500;
    private final static String SUCCESS_MESSAGE = "SUCCESS";
    private final static String NOT_FOUND_MESSAGE = "NOT FOUND";
    private final static String FAILED_MESSAGE = "서버에서 오류가 발생하였습니다.";
    private final static String INVALID_ACCESS_TOKEN = "Invalid access token.";
    private final static String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
    private final static String NOT_EXPIRED_TOKEN_YET = "Not expired token yet.";
    private final static String PERMISSION_DENIED = "권한이 없습니다.";
    private final static String NOT_CORRECTED_PASSWORD = "Bad Password.";
    private final static String NOT_CORRECTED_ID = "Bad ID.";
    private final static String DUPLICATED_EMAIL = "Duplicated Email.";
    private final static String NOT_EXIST = "It's a request for doesn't exist";

    private final APIResponseHeader header;
    private final T body;

    public static <T> APIResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);
        return new APIResponse(new APIResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
    }

    public static <T> APIResponse<T> success(T body) {
        return new APIResponse(new APIResponseHeader(SUCCESS, SUCCESS_MESSAGE), body);
    }

    public static <T> APIResponse<T> create(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);
        return new APIResponse(new APIResponseHeader(CREATE, SUCCESS_MESSAGE), map);
    }

    public static <T> APIResponse<T> delete() {
        return new APIResponse(new APIResponseHeader(NO_CONTENT, SUCCESS_MESSAGE), null);
    }

    public static <T> APIResponse<T> fail() {
        return new APIResponse(new APIResponseHeader(FAILED, FAILED_MESSAGE), null);
    }

    public static <T> APIResponse<T> invalidAccessToken() {
        return new APIResponse(new APIResponseHeader(FAILED, INVALID_ACCESS_TOKEN), null);
    }

    public static <T> APIResponse<T> invalidRefreshToken() {
        return new APIResponse(new APIResponseHeader(FAILED, INVALID_REFRESH_TOKEN), null);
    }

    public static <T> APIResponse<T> notExpiredTokenYet() {
        return new APIResponse(new APIResponseHeader(FAILED, NOT_EXPIRED_TOKEN_YET), null);
    }

    public static <T> APIResponse<T> permissionDenied() {
        return new APIResponse(new APIResponseHeader(FAILED, PERMISSION_DENIED), null);
    }

    public static <T> APIResponse<T> incorrectPassword() {
        return new APIResponse(new APIResponseHeader(FAILED, NOT_CORRECTED_PASSWORD), null);
    }

    public static <T> APIResponse<T> incorrectID() {
        return new APIResponse(new APIResponseHeader(FAILED, NOT_CORRECTED_ID), null);
    }
  
    public static <T> APIResponse<T> duplicatedEmail() {
        return new APIResponse(new APIResponseHeader(CONFLICT, DUPLICATED_EMAIL), null);
    }

    public static <T> APIResponse<T> notExistRequest() {
        return new APIResponse(new APIResponseHeader(NOT_FOUND, NOT_EXIST), null);
    }

}
