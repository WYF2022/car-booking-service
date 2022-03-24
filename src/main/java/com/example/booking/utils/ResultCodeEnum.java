package com.example.booking.utils;

import lombok.Getter;

/**
 * Global unified return status code
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "success"),
    FAIL(500, "failed");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
