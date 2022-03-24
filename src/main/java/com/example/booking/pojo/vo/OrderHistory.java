package com.example.booking.pojo.vo;

import com.example.booking.pojo.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistory {
    private Long id;
    private String userId;
    private Integer status;
    private Long createTime;
    private Long updateTime;
    private List<OrderDetail> orderDetailList;
}
