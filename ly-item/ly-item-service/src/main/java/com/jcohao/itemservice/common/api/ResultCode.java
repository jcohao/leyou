package com.jcohao.itemservice.common.api;

public enum ResultCode implements ErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILER(404, "参数校验失败"),
    UNAUTHORIZED(401, "暂未登录或者 token 已过期"),
    FORBIDDED(403, "没有相关权限，禁止访问");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
