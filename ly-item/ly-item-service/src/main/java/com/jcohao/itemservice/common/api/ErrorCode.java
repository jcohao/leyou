package com.jcohao.itemservice.common.api;


/**
 * 封装 api 错误码，定义返回结果的规范
 */
public interface ErrorCode {
    long getCode();

    String getMessage();
}
