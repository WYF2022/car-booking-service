package com.example.booking.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.booking.mapper.BookingOrderMapper;
import com.example.booking.mapper.CarStockMapper;
import com.example.booking.mapper.OrderDetailMapper;
import com.example.booking.mapper.OrderLogMapper;
import com.example.booking.pojo.dto.BookingCarDto;
import com.example.booking.pojo.dto.OrderHistoryDto;
import com.example.booking.pojo.entity.BookingOrder;
import com.example.booking.pojo.entity.CarStock;
import com.example.booking.pojo.entity.OrderDetail;
import com.example.booking.pojo.entity.OrderLog;
import com.example.booking.pojo.enums.OrderOpeStatusEnum;
import com.example.booking.pojo.enums.OrderStatusEnum;
import com.example.booking.pojo.vo.OrderHistory;
import com.example.booking.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * order service impl
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BookingOrderMapper bookingOrderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private CarStockMapper carStockMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * booking car
     *
     * @param bookingCarDto
     * @return
     */
    @Override
    @Transactional
    public Long booking(BookingCarDto bookingCarDto) throws Exception {
        // check stock
        if (this.redisTemplate.hasKey(bookingCarDto.getCarModel())
                && this.redisTemplate.opsForList().size(bookingCarDto.getCarModel())
                >= bookingCarDto.getBookingNum()) {
            for (int i = 0; i < bookingCarDto.getBookingNum(); i++) {
                this.redisTemplate.opsForList().rightPop(bookingCarDto.getCarModel());
            }
            // insert booking base table
            BookingOrder bookingOrder = BookingOrder.builder()
                    .userId(bookingCarDto.getUserId())
                    .status(OrderStatusEnum.SUCCESS.getValue())
                    .createTime(System.currentTimeMillis())
                    .updateTime(System.currentTimeMillis())
                    .build();
            this.bookingOrderMapper.insert(bookingOrder);
            // insert order detail
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderId(bookingOrder.getId())
                    .carModel(bookingCarDto.getCarModel())
                    .bookingNum(bookingCarDto.getBookingNum())
                    .startTime(bookingCarDto.getStartTime())
                    .endTime(bookingCarDto.getEndTime())
                    .status(OrderStatusEnum.SUCCESS.getValue())
                    .createTime(System.currentTimeMillis())
                    .updateTime(System.currentTimeMillis())
                    .build();
            this.orderDetailMapper.insert(orderDetail);
            this.handleOrderLog(bookingOrder, orderDetail, OrderOpeStatusEnum.CREATE);
            this.updateStock(bookingCarDto.getCarModel());
            return bookingOrder.getId();
        } else {
            throw new Exception("No quantity available in stock");
        }
    }

    /**
     * update order
     *
     * @param bookingCarDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Long update(BookingCarDto bookingCarDto) throws Exception {
        BookingOrder bookingOrder = this.bookingOrderMapper.selectById(bookingCarDto.getOrderId());
        if (bookingOrder == null || !bookingOrder.getUserId().equals(bookingCarDto.getUserId())) {
            throw new Exception("Not found current order");
        }

        EntityWrapper<OrderDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", bookingOrder.getId());
        List<OrderDetail> orderDetails = this.orderDetailMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(orderDetails)
                && this.redisTemplate.hasKey(bookingCarDto.getCarModel())
                && this.redisTemplate.opsForList().size(bookingCarDto.getCarModel())
                + orderDetails.get(0).getBookingNum() >= bookingCarDto.getBookingNum()) {
            if (bookingCarDto.getBookingNum() > orderDetails.get(0).getBookingNum()) {
                for (int i = 0; i < bookingCarDto.getBookingNum() - orderDetails.get(0).getBookingNum(); i++) {
                    this.redisTemplate.opsForList().rightPop(bookingCarDto.getCarModel());
                }
            }
            if (bookingCarDto.getBookingNum() < orderDetails.get(0).getBookingNum()) {
                for (int i = 0; i < orderDetails.get(0).getBookingNum() - bookingCarDto.getBookingNum(); i++) {
                    this.redisTemplate.opsForList().leftPush(bookingCarDto.getCarModel(), bookingCarDto.getCarModel());
                }
            }
            bookingOrder.setUpdateTime(System.currentTimeMillis());
            this.bookingOrderMapper.updateById(bookingOrder);
            OrderDetail orderDetail = orderDetails.get(0);
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setBookingNum(bookingCarDto.getBookingNum());
            orderDetail.setCarModel(bookingCarDto.getCarModel());
            orderDetail.setStartTime(bookingCarDto.getStartTime());
            orderDetail.setEndTime(bookingCarDto.getEndTime());
            this.orderDetailMapper.updateById(orderDetail);
            this.handleOrderLog(bookingOrder, orderDetail, OrderOpeStatusEnum.UPDATE);
            this.updateStock(bookingCarDto.getCarModel());
            return bookingOrder.getId();
        } else {
            throw new Exception("No quantity available in stock");
        }
    }

    /**
     * cancel order
     *
     * @param bookingCarDto
     * @return
     * @throws Exception
     */
    @Override
    public Long cancel(BookingCarDto bookingCarDto) throws Exception {
        BookingOrder bookingOrder = this.bookingOrderMapper.selectById(bookingCarDto.getOrderId());
        if (bookingOrder == null ||
                !bookingOrder.getUserId().equals(bookingCarDto.getUserId())) {
            throw new Exception("Not found order");
        }
        EntityWrapper<OrderDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", bookingOrder.getId());
        List<OrderDetail> orderDetails = this.orderDetailMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(orderDetails)) {
            OrderDetail orderDetail = orderDetails.get(0);
            for (int i = 0; i < orderDetail.getBookingNum(); i++) {
                this.redisTemplate.opsForList().leftPush(bookingCarDto.getCarModel(), bookingCarDto.getCarModel());
            }
            bookingOrder.setUpdateTime(System.currentTimeMillis());
            bookingOrder.setStatus(OrderStatusEnum.CANCEL.getValue());
            this.bookingOrderMapper.updateById(bookingOrder);
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setStatus(OrderOpeStatusEnum.CANCEL.getValue());
            this.orderDetailMapper.updateById(orderDetail);
            this.handleOrderLog(bookingOrder, orderDetail, OrderOpeStatusEnum.CANCEL);
        }
        return bookingOrder.getId();
    }

    /**
     * query order history
     *
     * @param orderHistoryDto
     * @return
     */
    @Override
    public List<OrderHistory> history(OrderHistoryDto orderHistoryDto) {
        List<OrderHistory> result = new ArrayList<>();
        EntityWrapper<BookingOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", orderHistoryDto.getUserId())
                .orderBy("create_time", false);
        Page<BookingOrder> page = new Page<>(orderHistoryDto.getPageNo(), orderHistoryDto.getPageSize());
        List<BookingOrder> list = this.bookingOrderMapper.selectPage(page, wrapper);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                OrderHistory orderHistory = OrderHistory.builder().build();
                BeanUtils.copyProperties(item, orderHistory);
                EntityWrapper<OrderDetail> wrap = new EntityWrapper<>();
                wrap.eq("order_id", item.getId())
                        .orderBy("create_time", false);
                orderHistory.setOrderDetailList(this.orderDetailMapper.selectList(wrap));
                result.add(orderHistory);
            });
        }
        return result;
    }

    /**
     * handel order operate log
     *
     * @param bookingOrder
     * @param orderDetail
     */
    private void handleOrderLog(BookingOrder bookingOrder, OrderDetail orderDetail, OrderOpeStatusEnum statusEnum) {
        // insert order log & handle operate version
        EntityWrapper<OrderLog> logWrapper = new EntityWrapper<>();
        logWrapper.eq("order_id", bookingOrder.getId())
                .orderBy("version", false);
        List<OrderLog> orderLogList = this.orderLogMapper.selectList(logWrapper);
        OrderLog orderLog = OrderLog.builder().build();
        BeanUtils.copyProperties(orderDetail, orderLog);
        orderLog.setId(null);
        orderLog.setStatus(statusEnum.getValue());
        if (CollectionUtils.isEmpty(orderLogList)) {
            orderLog.setVersion(0);
        } else {
            orderLog.setVersion(orderLogList.get(0).getVersion() + 1);
        }
        this.orderLogMapper.insert(orderLog);
    }

    /**
     * update mysql stock
     *
     * @param carModel
     */
    private void updateStock(String carModel) {
        if (this.redisTemplate.opsForList().size(carModel) == 0) {
            EntityWrapper<CarStock> wrapper = new EntityWrapper<>();
            wrapper.eq("car_model", carModel);
            List<CarStock> carStocks = this.carStockMapper.selectList(wrapper);
            if (!CollectionUtils.isEmpty(carStocks)) {
                CarStock carStock = carStocks.get(0);
                carStock.setStock(0);
                this.carStockMapper.updateById(carStock);
            }
        }
    }
}
