package com.example.booking.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_booking_order")
public class BookingOrder {
    private Long id;
    private String userId;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
