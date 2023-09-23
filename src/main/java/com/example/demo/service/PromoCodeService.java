package com.example.demo.service;


import com.example.demo.dto.promo_code_dto.PromoCodeCreateDto;
import com.example.demo.dto.promo_code_dto.PromoCodeGetDto;
import com.example.demo.dto.promo_code_dto.PromoCodeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PromoCodeService {
    PromoCodeGetDto save(PromoCodeCreateDto dto);
    PromoCodeGetDto update(PromoCodeUpdateDto dto);
    void delete(UUID id);
    PromoCodeGetDto get(UUID id);
    Page<PromoCodeGetDto> promoCodes(UUID goodId,Pageable pageable);
    PromoCodeGetDto getByName(String name);
}
