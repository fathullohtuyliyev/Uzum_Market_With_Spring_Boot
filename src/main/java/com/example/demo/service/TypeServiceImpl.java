package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;
import static com.example.demo.mapper.TypeMapper.TYPE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    @Override
    public TypeGetDto save(TypeCreateDto dto) {
        try {
            Type type = TYPE_MAPPER.toEntity(dto);
            Type saved = typeRepository.save(type);
            TypeGetDto dto1 = TYPE_MAPPER.toDto(saved);
            log.info("{} created",dto1);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public TypeGetDto update(TypeUpdateDto dto) {
        try {
            Type type = TYPE_MAPPER.toEntity(dto);
            typeRepository.updateType(type.getName(), type.getId(), type.getSub(), type.getRoot());
            Type found = typeRepository.findById(type.getId()).orElseThrow(NotFoundException::new);
            TypeGetDto dto1 = TYPE_MAPPER.toDto(found);
            method(found,dto1);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void method(Type found, TypeGetDto dto) {
        List<GoodGetDto> list = found.getGoods()
                .stream()
                .map(GOOD_MAPPER::toDto)
                .toList();
        dto.setGoods(list);
    }

    @Override
    public TypeGetDto get(Long id) {
        try {
            Type type = typeRepository.findById(id).orElseThrow(NotFoundException::new);
            TypeGetDto dto = TYPE_MAPPER.toDto(type);
            method(type,dto);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<TypeGetDto> types(Pageable pageable) {
        try {
            int size = typeRepository.size();
            Page<Type> all = typeRepository.findAll(pageable);
            if (size<pageable.getPageSize()) {
                all = new PageImpl<>(typeRepository.findAll(), PageRequest.of(0,size),size);
            }
            List<TypeGetDto> list = all.stream()
                    .map(TYPE_MAPPER::toDto)
                    .toList();
            List<Type> content = all.getContent();
            for (int i = 0; i < list.size(); i++) {
                method(content.get(i),list.get(i));
            }
            return new PageImpl<>(list,all.getPageable(),list.size());
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
