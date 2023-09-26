package com.example.demo.mapper;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "promoCodes",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "data",ignore = true)
    @Mapping(target = "online",ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "activateCodes",ignore = true)
    @Mapping(target = "temporaryPassword",ignore = true)
    @Mapping(target = "roles",ignore = true)
    AuthUser toEntity(AuthUserGetDto dto);

    @Mapping(target = "promoCodes",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "data",ignore = true)
    @Mapping(target = "online",ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "activateCodes",ignore = true)
    @Mapping(target = "temporaryPassword",ignore = true)
    @Mapping(target = "roles",ignore = true)
    AuthUser toEntity(AuthUserCreateDto dto);

    @Mapping(target = "promoCodes",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "data",ignore = true)
    @Mapping(target = "online",ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "phone",ignore = true)
    @Mapping(target = "email",ignore = true)
    @Mapping(target = "activateCodes",ignore = true)
    @Mapping(target = "temporaryPassword",ignore = true)
    @Mapping(target = "roles",ignore = true)
    AuthUser toEntity(AuthUserUpdateDto dto);

    @Mapping(target = "roles",ignore = true)
    AuthUserGetDto toDto(AuthUser authUser);

    default Page<AuthUserGetDto> toDto(Page<AuthUser> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<AuthUserGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
