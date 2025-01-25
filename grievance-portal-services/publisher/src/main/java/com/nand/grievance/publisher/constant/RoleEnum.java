package com.nand.grievance.publisher.constant;

public enum RoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
