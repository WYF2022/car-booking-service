package com.example.booking.schedule;

import com.example.booking.pojo.entity.CarStock;
import com.example.booking.service.CarStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Refresh the inventory in the database to redis
 */
@Configuration
@EnableScheduling
@Slf4j
public class CarStockSchedule {

    @Autowired
    private CarStockService carStockService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * run every five minutes
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void configureTasks() {
        List<CarStock> list = this.carStockService.list();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(carStock -> {
                log.info("begin put {} stock to redis", carStock.getCarModel());
                // put the inventory to redis only once
                if (!this.redisTemplate.hasKey(carStock.getCarModel())) {
                    // convert inventory quantity into queue element
                    Integer stock = carStock.getStock();
                    List<String> carModelIds = new ArrayList<>();
                    if (stock != null && stock > 0) {
                        for (int i = 0; i < stock; i++) {
                            carModelIds.add(carStock.getId().toString());
                        }
                    }
                    this.redisTemplate.opsForList().leftPushAll(carStock.getCarModel(), carModelIds);
                }
                log.info("put {} stock to redis complete, stock {}", carStock.getCarModel(), carStock.getStock());
            });
        }
    }
}
