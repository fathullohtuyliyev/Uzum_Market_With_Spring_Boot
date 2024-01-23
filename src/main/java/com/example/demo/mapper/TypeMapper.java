package com.example.demo.mapper;

import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TypeRepository;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface TypeMapper {
    TypeMapper TYPE_MAPPER = Mappers.getMapper(TypeMapper.class);

    default Type toEntity(@NonNull TypeGetDto dto, TypeRepository typeRepository){
        Type type = Type.builder().id(dto.id).name(dto.name).build();
        Long subId = dto.subId;
        Type root = typeRepository.findById(dto.rootId)
                .orElseThrow(NotFoundException::new);
        if (subId!=null) {
            typeRepository.findById(subId).ifPresent(type::setSub);
        }
        if (root==null) {
            return type;
        }
        Type foundType = typeRepository.findById(root.getId())
                .orElseThrow(NotFoundException::new);
        type.setRoot(foundType);
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
        Long rootId = dto.rootId;
        if (subId!=null) {
            typeRepository.findById(subId).ifPresent(type::setSub);
        }
        if (rootId==null) {
            return type;
        }
        Type foundType = typeRepository.findById(rootId)
                .orElseThrow(NotFoundException::new);
        type.setRoot(foundType);
        return type;
    }

    default TypeGetDto toDto(@NonNull Type type){
        Type root = type.getRoot();
        return TypeGetDto
                .builder()
                .id(type.getId())
                .name(type.getName())
                .subId(type.getSub().getId())
                .rootId(root.getId())
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
