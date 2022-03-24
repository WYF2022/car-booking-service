package com.example.booking.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order operate status enum
 */
@Getter
@AllArgsConstructor
public enum OrderOpeStatusEnum {

    CREATE(1),

    UPDATE(2),

    CANCEL(3),

    DELETE(4);

    private Integer value;

}
