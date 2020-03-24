package com.jcohao.itemservice.common.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {

    private long code;
    private String message;
    private T data;

    // 成功返回结果
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 成功返回结果，附加 message
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    // 失败返回结果
    public static <T> CommonResult<T> failed(ErrorCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    // 失败返回结果，附加 message
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(ResultCode.FAILED.getCode(), message, null);
    }

    // 返回失败结果，无参版本
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    // 参数校验失败返回失败结果
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILER);
    }


    // 参数校验失败返回失败结果，附加参数
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<>(ResultCode.VALIDATE_FAILER.getCode(), message, null);
    }

    // 未登录或 token 过期返回结果
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    // 未授权返回结果
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<>(ResultCode.FORBIDDED.getCode(), ResultCode.FORBIDDED.getMessage(), data);
    }

}
