package com.weblab.common.enums;

public enum RoleEnum {
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    RoleEnum(String role) { this.role = role; }

    public String value() { return role; }
}