package com.example.booking.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order status enum
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    SUCCESS(1),

    FAILED(2),

    CANCEL(3),

    DELETE(4);

    private Integer value;

}
