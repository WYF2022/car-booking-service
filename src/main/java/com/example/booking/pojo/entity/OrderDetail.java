package com.example.booking.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order_detail")
public class OrderDetail {
    private Long id;
    private Long orderId;
    private String carModel;
    private Integer bookingNum;
    private Long startTime;
    private Long endTime;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
