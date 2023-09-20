package com.example.demo.service;

import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    @Override
    public DeliveryGetDto save(DeliveryCreateDto dto) {
        return null;
    }

    @Override
    public DeliveryGetDto update(DeliveryUpdateDto dto) {
        return null;
    }


    @Override
    public DeliveryGetDto get(UUID id) {
        return null;
    }

    @Override
    public Page<DeliveryGetDto> users(UUID userId) {
        return null;
    }
}
