package cn.alexpy.common.utils;

public enum HttpCodeUtil {

    UNAUTHORIZED(401, "登录过期, 请重新登录"),
    FAIL(406, "操作失败"),
    TOO_MANY_REQUESTS(429, "非法访问");

    private int code;

    private String message;

    HttpCodeUtil(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
