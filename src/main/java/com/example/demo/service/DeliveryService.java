package com.example.demo.service;


import com.example.demo.dto.basket_dto.BasketCreateDto;
import com.example.demo.dto.basket_dto.BasketGetDto;
import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryService {
    DeliveryGetDto save(DeliveryCreateDto dto);
    DeliveryGetDto update(DeliveryUpdateDto dto);
    DeliveryGetDto get(Long id);
    Page<DeliveryGetDto> deliveryPoints(Pageable pageable);
}
