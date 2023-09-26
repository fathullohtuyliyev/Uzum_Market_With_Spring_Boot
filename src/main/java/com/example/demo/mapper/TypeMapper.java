package com.example.demo.mapper;

import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import com.example.demo.entity.Type;
import com.example.demo.repository.TypeRepository;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper
public interface TypeMapper {
    TypeMapper TYPE_MAPPER = Mappers.getMapper(TypeMapper.class);

    default Type toEntity(@NonNull TypeGetDto dto, TypeRepository typeRepository){
        Type type = Type.builder().id(dto.id).name(dto.name).build();
        Long subId = dto.subId;
        List<TypeGetDto> roots = dto.roots;
        if (subId!=null) {
            typeRepository.findById(subId).ifPresent(type::setSub);
        }
        if (roots==null || roots.isEmpty()) {
            return type;
        }
        List<Long> rootsId = roots.stream()
                .map(dto1 -> dto1.id)
                .toList();
        List<Type> allById = typeRepository.findAllById(rootsId);
        type.setRoots(allById);
        return type;
    }

    default Type toEntity(@NonNull TypeCreateDto dto, TypeRepository typeRepository){
        Type type = Type.builder().name(dto.name).build();
        method1(type, dto, typeRepository);
        return type;
    }
    default void method1(Type type, TypeCreateDto dto ,
                         TypeRepository typeRepository){
        Long subId = dto.subId;
        if (subId!=null) {
            typeRepository.findById(subId).ifPresent(type::setSub);
        }
    }

    default Type toEntity(@NonNull TypeUpdateDto dto, TypeRepository typeRepository){
        Type type = Type.builder().name(dto.name).id(dto.id).build();
        Long subId = dto.subId;
        Set<Long> rootsId = dto.rootsId;
        if (subId!=null) {
            typeRepository.findById(subId).ifPresent(type::setSub);
        }
        if (rootsId==null || rootsId.isEmpty()) {
            return type;
        }
        List<Type> allById = typeRepository.findAllById(rootsId);
        type.setRoots(allById);
        return type;
    }

    default TypeGetDto toDto(@NonNull Type type){
        List<TypeGetDto> rootList = type.getRoots()
                .stream()
                .map(rootType -> TypeGetDto.builder()
                        .id(rootType.getId())
                        .name(rootType.getName())
                        .subId(type.getId())
                        .roots(Collections.emptyList())
                        .build()).toList();
        return TypeGetDto
                .builder()
                .id(type.getId())
                .name(type.getName())
                .subId(type.getSub().getId())
                .roots(rootList)
                .build();
    }

    default Page<TypeGetDto> toDto(Page<Type> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<TypeGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
