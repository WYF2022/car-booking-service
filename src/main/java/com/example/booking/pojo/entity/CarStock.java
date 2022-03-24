package com.example.booking.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * car stock entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_car_stock")
public class CarStock {
    private Long id;
    private String carModel;
    private Integer stock;
}
