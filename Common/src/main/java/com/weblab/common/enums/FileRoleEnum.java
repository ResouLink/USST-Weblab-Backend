package com.weblab.common.enums;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum FileRoleEnum {
    QUESTION(0, "问题附件"),
    ANSWER(1, "回答附件"),
    RESOURCE(2, "资源附件"),
    AVATAR(3, "头像数据"),
    ;
    private final Integer fileRole;
    private final String fileRoleName;


    public Integer getFileRole() {
        return fileRole;
    }
    public String getFileRoleName() {
        return fileRoleName;
    }

}
