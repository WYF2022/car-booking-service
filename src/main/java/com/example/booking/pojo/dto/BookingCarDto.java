package com.example.booking.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * booking car dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCarDto {
    private Long orderId;
    private String userId;
    private String carModel;
    private Integer bookingNum;
    private Long startTime;
    private Long endTime;
}
