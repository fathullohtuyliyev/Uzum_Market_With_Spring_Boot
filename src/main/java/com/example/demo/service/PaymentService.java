package com.example.demo.service;


import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    PaymentGetDto save(String name);
    PaymentGetDto update(PaymentUpdateDto dto);
    void delete(UUID id);
    PaymentGetDto get(UUID id);
    Page<PaymentGetDto> users(UUID userId);
}
