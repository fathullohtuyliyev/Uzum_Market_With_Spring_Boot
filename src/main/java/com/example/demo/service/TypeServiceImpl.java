package com.example.demo.service;

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
import java.util.Collection;
import java.util.List;
import static com.example.demo.mapper.TypeMapper.TYPE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    @Override
    public TypeGetDto save(TypeCreateDto dto) {
        try {
            Type type = TYPE_MAPPER.toEntity(dto,typeRepository);
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
            Type type = TYPE_MAPPER.toEntity(dto,typeRepository);
            typeRepository.updateType(type.getName(), type.getId(), type.getSub(), type.getRoots());
            Type found = typeRepository.findById(type.getId()).orElseThrow(NotFoundException::new);
            TypeGetDto dto1 = TYPE_MAPPER.toDto(found);
            log.info("{} updated", dto1);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }


    @Override
    public TypeGetDto get(Long id) {
        try {
            Type type = typeRepository.findById(id).orElseThrow(NotFoundException::new);
            TypeGetDto dto = TYPE_MAPPER.toDto(type);
            log.info("{} gave",dto);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public List<TypeGetDto> getAllByIds(Collection<Long> ids) {
        try {
            return typeRepository.findAllById(ids)
                    .stream()
                    .map(TYPE_MAPPER::toDto)
                    .toList();
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
            return TYPE_MAPPER.toDto(all);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public List<TypeGetDto> allSubTypes() {
        try {
            List<Type> types = typeRepository.allSubTypes();
            return types.stream()
                    .map(TYPE_MAPPER::toDto)
                    .toList();
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
