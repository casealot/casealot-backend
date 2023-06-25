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
    private final static String NOT_CORRECTED_PASSWORD = "비밀번호가 올바르지 않습니다.";
    private final static String NOT_CORRECTED_ID = "아이디가 올바르지 않습니다.";
    private final static String DUPLICATED_EMAIL = "이미 존재하는 이메일입니다.";
    private final static String DUPLICATED_ID = "이미 존재하는 아이디입니다.";
    private final static String NOT_EXIST = "존재하지 않는 무언가에 대한 요청입니다."; //얘 바꿔야할듯
    private final static String WISH_ALREADY_EXIST = "위시리스트에 상품이 이미 등록되어 있습니다.";
    private final static String NULL_CHECK = "Please check null"; //얘는 뭐로바꾸지..
    private final static String DUPLICATED_PRODUCT = "상품명은 중복될 수 없습니다.";

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
}
