package com.weblab.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorEnum implements BaseEnum {
    Fail(0, "操作失败"),
    NotFindError(10001, "未查询到信息"),
    SaveError(10002, "保存信息失败"),
    UpdateError(10003, "更新信息失败"),
    DeleteError(10004, "删除信息失败");

    private final Integer errorCode; // 业务错误码
    private final String errorMessage; // 业务错误码描述

    @Override
    public Integer getCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
