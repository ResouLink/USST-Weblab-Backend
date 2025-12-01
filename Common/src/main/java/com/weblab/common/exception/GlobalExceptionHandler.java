package com.weblab.common.exception;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResult<String>> handleBusinessException(ServiceException e) {
        log.error("业务异常", e);
        if (BeanUtil.isEmpty(e.getMessage())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.fail(ErrorEnum.Fail.getCode(), e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.fail(ErrorEnum.Fail.getCode(), e.getMessage()));
    }
}
