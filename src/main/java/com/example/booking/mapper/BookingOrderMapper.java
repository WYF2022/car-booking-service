package com.example.booking.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.booking.pojo.entity.BookingOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * booking car order mapper
 */
@Mapper
public interface BookingOrderMapper extends BaseMapper<BookingOrder> {

}
