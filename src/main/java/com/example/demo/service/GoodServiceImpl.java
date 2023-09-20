package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {
    @Override
    public GoodGetDto save(GoodCreateDto dto) {
        return null;
    }

    @Override
    public GoodGetDto update(GoodUpdateDto dto) {
        return null;
    }

    @Override
    public GoodGetDto get(UUID id) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable) {
        return null;
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, String name) {
        return null;
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, GoodCriteria criteria) {
        return null;
    }
}
