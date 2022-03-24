package com.example.booking.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.booking.pojo.entity.CarStock;
import com.example.booking.mapper.CarStockMapper;
import com.example.booking.service.CarStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * CarStockService impl
 */
@Service
public class CarStockServiceImpl extends ServiceImpl<CarStockMapper, CarStock> implements CarStockService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CarStockMapper carStockMapper;

    /**
     * list car stock
     *
     * @return
     */
    @Override
    public List<CarStock> list() {
        List<CarStock> list = this.carStockMapper.selectList(null);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                // obtain stock in real time from redis
                if (this.redisTemplate.hasKey(item.getCarModel())) {
                    item.setStock(this.redisTemplate.opsForList().size(item.getCarModel()).intValue());
                }
            });
        }
        return list;
    }
}
