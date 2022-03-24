package com.example.booking.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.booking.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * booking car order detail mapper
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
