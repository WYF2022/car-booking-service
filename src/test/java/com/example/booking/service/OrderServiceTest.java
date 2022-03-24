package com.example.booking.service;

import com.example.booking.mapper.BookingOrderMapper;
import com.example.booking.mapper.OrderDetailMapper;
import com.example.booking.mapper.OrderLogMapper;
import com.example.booking.pojo.dto.BookingCarDto;
import com.example.booking.service.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private BookingOrderMapper bookingOrderMapper;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    @Mock
    private OrderLogMapper orderLogMapper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ListOperations listOperations;


    @Test
    public void booking_test_ok() throws Exception {
        BookingCarDto bookingCarDto = BookingCarDto.builder()
                .carModel("model")
                .bookingNum(1)
                .build();
        Mockito.when(this.redisTemplate.hasKey(bookingCarDto.getCarModel())).thenReturn(true);
        Mockito.when(this.redisTemplate.opsForList()).thenReturn(this.listOperations);
        Mockito.when(this.listOperations.size(bookingCarDto.getCarModel())).thenReturn(2L);
        this.orderService.booking(bookingCarDto);
        Mockito.verify(this.bookingOrderMapper, Mockito.times(1)).insert(Mockito.any());
        Mockito.verify(this.orderDetailMapper, Mockito.times(1)).insert(Mockito.any());
        Mockito.verify(this.orderLogMapper, Mockito.times(1)).insert(Mockito.any());
    }

    @Test
    public void booking_test_exception() {
        BookingCarDto bookingCarDto = BookingCarDto.builder()
                .carModel("model")
                .bookingNum(1)
                .build();
        Mockito.when(this.redisTemplate.hasKey(bookingCarDto.getCarModel())).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> this.orderService.booking(bookingCarDto));
        assertEquals(exception.getMessage(), "No quantity available in stock");
        Mockito.verify(this.bookingOrderMapper, Mockito.times(0)).insert(Mockito.any());
        Mockito.verify(this.orderDetailMapper, Mockito.times(0)).insert(Mockito.any());
        Mockito.verify(this.orderLogMapper, Mockito.times(0)).insert(Mockito.any());
    }
}
