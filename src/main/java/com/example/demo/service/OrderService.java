package com.example.demo.service;

import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {
    OrderGetDto save(OrderCreateDto dto);
    OrderGetDto update(OrderUpdateDto dto);
    OrderGetDto updateStatus(UUID id, String statusName);
    OrderGetDto get(UUID id);
    Page<OrderGetDto> users(UUID userId, Pageable pageable);
}
