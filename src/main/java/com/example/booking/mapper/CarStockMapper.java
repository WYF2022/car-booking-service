package com.example.booking.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.booking.pojo.entity.CarStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * car stock mapper
 */
@Mapper
public interface CarStockMapper extends BaseMapper<CarStock> {
}
