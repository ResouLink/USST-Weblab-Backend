package com.weblab.common.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class ServiceException extends RuntimeException {
  ErrorEnum errorCode;

  public ServiceException() {
    super();
    this.errorCode = ErrorEnum.Fail;
  }
  public ServiceException(String msg) {
    super(msg);
    this.errorCode = ErrorEnum.Fail;
  }
  public ServiceException(ErrorEnum errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
  public ServiceException(ErrorEnum errorCode, String msg) {
    super(msg);
    this.errorCode = errorCode;
  }
}
