package com.example.demo.service;

import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentGetDto save(String name) {
        return null;
    }

    @Override
    public PaymentGetDto update(PaymentUpdateDto dto) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public PaymentGetDto get(UUID id) {
        return null;
    }

    @Override
    public Page<PaymentGetDto> users(UUID userId) {
        return null;
    }
}
