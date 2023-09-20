package com.example.demo.mapper;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface TypeMapper {
    TypeMapper TYPE_MAPPER = Mappers.getMapper(TypeMapper.class);

    @Mapping(target = "goods")
    Type toEntity(TypeGetDto dto);

    @Mapping(target = "goods")
    @Mapping(target = "id")
    Type toEntity(TypeCreateDto dto);

    @Mapping(target = "goods")
    Type toEntity(TypeUpdateDto dto);

    @Mapping(target = "goods")
    TypeGetDto toDto(Type type);

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
