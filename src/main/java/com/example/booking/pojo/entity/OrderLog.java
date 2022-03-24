package com.example.booking.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order operate log
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order_log")
public class OrderLog {
    private Long id;
    private Long orderId;
    private String carModel;
    private Integer bookingNum;
    private Long startTime;
    private Long endTime;
    private Integer version;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
