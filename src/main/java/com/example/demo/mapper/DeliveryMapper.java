package com.example.demo.mapper;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.DeliveryPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface DeliveryMapper {
    DeliveryMapper DELIVERY_MAPPER = Mappers.getMapper(DeliveryMapper.class);
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deliveryAddress",ignore = true)
    DeliveryPoint toEntity(DeliveryCreateDto dto);

    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "deliveryAddress",ignore = true)
    DeliveryPoint toEntity(DeliveryUpdateDto dto);

    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "deliveryAddress",ignore = true)
    DeliveryPoint toEntity(DeliveryGetDto dto);

    @Mapping(target = "deliveryAddress",ignore = true)
    DeliveryGetDto toDto(DeliveryPoint deliveryPoint);

    default Page<DeliveryGetDto> toDto(Page<DeliveryPoint> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<DeliveryGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
