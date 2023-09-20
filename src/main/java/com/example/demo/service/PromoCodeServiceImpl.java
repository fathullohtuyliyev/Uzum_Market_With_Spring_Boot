package com.example.demo.service;

import com.example.demo.dto.promo_code_dto.PromoCodeCreateDto;
import com.example.demo.dto.promo_code_dto.PromoCodeGetDto;
import com.example.demo.dto.promo_code_dto.PromoCodeUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    @Override
    public PromoCodeGetDto save(PromoCodeCreateDto dto) {
        return null;
    }

    @Override
    public PromoCodeGetDto update(PromoCodeUpdateDto dto) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public PromoCodeGetDto get(UUID id) {
        return null;
    }

    @Override
    public Page<PromoCodeGetDto> users(UUID goodId, Pageable pageable) {
        return null;
    }
}
