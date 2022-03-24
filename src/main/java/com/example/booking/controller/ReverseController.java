package com.example.booking.controller;

import com.example.booking.pojo.dto.BookingCarDto;
import com.example.booking.pojo.dto.OrderHistoryDto;
import com.example.booking.pojo.entity.CarStock;
import com.example.booking.pojo.vo.OrderHistory;
import com.example.booking.service.CarStockService;
import com.example.booking.service.OrderService;
import com.example.booking.utils.Result;
import com.example.booking.utils.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * car reverse api
 */
@Api(description = "Car Booking API")
@RestController
@RequestMapping("car/")
public class ReverseController {

    @Autowired
    private CarStockService carStockService;

    @Autowired
    private OrderService orderService;


    /**
     * query car stock
     *
     * @return
     */
    @ApiOperation(value = "list current stock", notes = "query current car stock")
    @GetMapping("list")
    public Result<List<CarStock>> list() {
        return Result.build(this.carStockService.list(), ResultCodeEnum.SUCCESS);
    }

    /**
     * booking
     *
     * @param bookingCarDto
     * @return
     */
    @ApiOperation(value = "booking", notes = "booking car")
    @PostMapping("booking")
    public Result booking(@RequestBody BookingCarDto bookingCarDto) {
        try {
            Long orderId = this.orderService.booking(bookingCarDto);
            return Result.build(orderId, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            return Result.build(e.getMessage(), ResultCodeEnum.FAIL);
        }
    }

    /**
     * update booking
     *
     * @param bookingCarDto
     * @return
     */
    @ApiOperation(value = "update booking", notes = "update booking")
    @PutMapping("booking")
    public Result update(@RequestBody BookingCarDto bookingCarDto) {
        try {
            Long orderId = this.orderService.update(bookingCarDto);
            return Result.build(orderId, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            return Result.build(e.getMessage(), ResultCodeEnum.FAIL);
        }
    }

    /**
     * update booking
     *
     * @param bookingCarDto
     * @return
     */
    @ApiOperation(value = "cancel booking", notes = "cancel booking")
    @DeleteMapping("booking")
    public Result cancel(@RequestBody BookingCarDto bookingCarDto) {
        try {
            Long orderId = this.orderService.cancel(bookingCarDto);
            return Result.build(orderId, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            return Result.build(e.getMessage(), ResultCodeEnum.FAIL);
        }
    }

    /**
     * query history order
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "query history", notes = "query history")
    @GetMapping("booking/history")
    public Result<List<OrderHistory>> history(@RequestParam("userId") String userId,
                                              @RequestParam("pageNo") Integer pageNo,
                                              @RequestParam("pageSize") Integer pageSize) {
        OrderHistoryDto orderHistoryDto = OrderHistoryDto.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .userId(userId)
                .build();
        return Result.build(this.orderService.history(orderHistoryDto), ResultCodeEnum.SUCCESS);
    }
}
