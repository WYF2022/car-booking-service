package com.example.booking.service;

import com.example.booking.pojo.dto.BookingCarDto;
import com.example.booking.pojo.dto.OrderHistoryDto;
import com.example.booking.pojo.vo.OrderHistory;

import java.util.List;

/**
 * order service
 */
public interface OrderService {
    /**
     * booking car
     *
     * @param bookingCarDto
     * @return
     */
    Long booking(BookingCarDto bookingCarDto) throws Exception;

    /**
     * update order
     *
     * @param bookingCarDto
     * @return
     * @throws Exception
     */
    Long update(BookingCarDto bookingCarDto) throws Exception;

    /**
     * cancel order
     *
     * @param bookingCarDto
     * @return
     * @throws Exception
     */
    Long cancel(BookingCarDto bookingCarDto) throws Exception;

    /**
     * query order history
     * @param orderHistoryDto
     * @return
     */
    List<OrderHistory> history(OrderHistoryDto orderHistoryDto);

}
