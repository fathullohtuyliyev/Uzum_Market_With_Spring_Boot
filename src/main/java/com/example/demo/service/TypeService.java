package com.example.demo.service;


import java.util.Collection;
import java.util.List;
import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TypeService {
    TypeGetDto save(TypeCreateDto dto);
    TypeGetDto update(TypeUpdateDto dto);
    TypeGetDto get(Long id);
    List<TypeGetDto> getAllByIds(Collection<Long> ids);
    Page<TypeGetDto> types(Pageable pageable);
    List<TypeGetDto> allSubTypes();
}
