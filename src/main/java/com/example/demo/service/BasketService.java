package com.example.demo.service;


import com.example.demo.dto.basket_dto.BasketCreateDto;
import com.example.demo.dto.basket_dto.BasketGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface BasketService {
    Page<BasketGetDto> save(BasketCreateDto dto);
    Page<BasketGetDto> delete(UUID goodId,UUID userId);
    Page<BasketGetDto> get(UUID userId, Pageable pageable);
}
