package com.weblab.common.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class ServiceException extends RuntimeException {
  BaseEnum resultCode;

  public ServiceException(String msg) {
    super(msg);
    this.resultCode = ResultCode.Fail;
  }
  public ServiceException(BaseEnum resultCode) {
    super(resultCode.getName());
    this.resultCode = resultCode;
  }
  public ServiceException(BaseEnum resultCode, String msg) {
    super(msg);
    this.resultCode = resultCode;
  }
}
