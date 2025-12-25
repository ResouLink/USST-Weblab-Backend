package com.weblab.common.enums;

public enum RoleEnum {
    TEACHER("ROLE_TEACHER",0),
    STUDENT("ROLE_STUDENT",1),
    ADMIN("ROLE_ADMIN",2);

    private final String role;
    private final Integer num;

    RoleEnum(String role, Integer num) {
        this.role = role;
        this.num = num;
    }

    public String value() { return role; }
    public Integer num() { return num; }
}