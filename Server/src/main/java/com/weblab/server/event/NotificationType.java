package com.weblab.server.event;

import com.weblab.common.exception.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    QUESTION(0, "问题"),
    ANSWER(1, "回答");

    private final Integer code;
    private final String message;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
