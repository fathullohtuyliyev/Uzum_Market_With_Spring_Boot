package com.example.demo.service;


import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PaymentTypeService {
    PaymentGetDto save(String name);
    PaymentGetDto update(PaymentUpdateDto dto);
    void delete(Integer id);
    PaymentGetDto get(Integer id);
    Page<PaymentGetDto> paymentTypes(Pageable pageable);
}
