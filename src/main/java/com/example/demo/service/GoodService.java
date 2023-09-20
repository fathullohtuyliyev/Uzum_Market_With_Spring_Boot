package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface GoodService {
    GoodGetDto save(GoodCreateDto dto);
    GoodGetDto update(GoodUpdateDto dto);
    GoodGetDto get(UUID id);
    void delete(UUID id);
    Page<GoodGetDto> find(Pageable pageable);
    Page<GoodGetDto> find(Pageable pageable,String name);
    Page<GoodGetDto> find(Pageable pageable, GoodCriteria criteria);
}
