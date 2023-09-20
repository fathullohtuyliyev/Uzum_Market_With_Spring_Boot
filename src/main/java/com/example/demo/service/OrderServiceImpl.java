package com.example.demo.service;

import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderGetDto save(OrderCreateDto dto) {
        return null;
    }

    @Override
    public OrderGetDto update(OrderUpdateDto dto) {
        return null;
    }

    @Override
    public OrderGetDto get(UUID id) {
        return null;
    }

    @Override
    public Page<OrderGetDto> users(UUID userId) {
        return null;
    }
}
