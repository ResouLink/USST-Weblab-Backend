//package com.weblab.common.exception;
//
//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
//public enum ResultCode implements BaseEnum {
//    Success(1, "操作成功"),
//    Fail(0, "操作失败"),
//    NotFindError(10001, "未查询到信息"),
//    SaveError(10002, "保存信息失败"),
//    UpdateError(10003, "更新信息失败");
//
//    private final Integer code; // 业务错误码
//    private final String name; // 业务错误码描述
//
//    @Override
//    public Integer getCode() {
//        return this.code;
//    }
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//}
