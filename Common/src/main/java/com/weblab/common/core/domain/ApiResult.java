package com.weblab.common.core.domain;

import lombok.AllArgsConstructor;

/**
 * 基础返回体
 * @param <T>
 */
@AllArgsConstructor
public class ApiResult<T> {
    // 状态码
    private Integer code;
    // 返回消息
    private String message;
    // 返回数据
    private T data;

    public ApiResult() {
    }

    public static <T> ApiResult<T> restApiResult(Integer code, String message, T data){
        return new ApiResult<>(code, message, data);
    }

    // 成功
    public static <T> ApiResult<T> success(T data){
        return restApiResult(200, "success", data);
    }
    public static <T> ApiResult<T> success(String message, T data){
        return restApiResult(200, message, data);
    }
    // 失败
    public static <T> ApiResult<T> fail(Integer code, String message){
        return restApiResult(code, message, null);
    }

    public static <T> ApiResult<T> fail(String message){
        return restApiResult(500, message, null);
    }
}
