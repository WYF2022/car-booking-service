package com.example.booking.service;


import com.example.booking.pojo.entity.CarStock;

import java.util.List;

/**
 * car stock service
 */
public interface CarStockService {
    /**
     * query car stock
     *
     * @return
     */
    List<CarStock> list();
}
