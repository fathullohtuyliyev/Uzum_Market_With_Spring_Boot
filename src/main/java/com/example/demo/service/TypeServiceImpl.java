package com.example.demo.service;

import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    @Override
    public TypeGetDto save(TypeCreateDto dto) {
        return null;
    }

    @Override
    public TypeGetDto update(TypeUpdateDto dto) {
        return null;
    }

    @Override
    public TypeGetDto get(Long id) {
        return null;
    }

    @Override
    public Page<TypeGetDto> types(Pageable pageable) {
        return null;
    }
}
