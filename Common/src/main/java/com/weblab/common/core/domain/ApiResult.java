package com.weblab.common.core.domain;

import com.weblab.common.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础返回体
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ApiResult<T> implements Serializable {
    // 状态码
    private Integer code;
    // 返回消息
    private String message;
    // 返回数据
    private T data;

    public ApiResult() {
    }

    public static <T> ApiResult<T> restApiResult(Integer code, String message, T data){
        return new ApiResult<T>(code, message, data);
    }

    // 成功
    public static <T> ApiResult<T> success(T data){
        return restApiResult(HttpStatus.SUCCESS, "success", data);
    }
    public static <T> ApiResult<T> success(String message, T data){
        return restApiResult(HttpStatus.SUCCESS, message, data);
    }
    // 失败
    public static <T> ApiResult<T> fail(Integer code, String message){
        return restApiResult(code, message, null);
    }

    public static <T> ApiResult<T> fail(String message){
        return restApiResult(HttpStatus.SUCCESS, message, null);
    }
}
