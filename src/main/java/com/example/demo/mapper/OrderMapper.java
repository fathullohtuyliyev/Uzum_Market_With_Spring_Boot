package com.example.demo.mapper;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);
    @Mapping(target = "authUser",ignore = true)
    @Mapping(target = "good",ignore = true)
    @Mapping(target = "deliveryPoint",ignore = true)
    @Mapping(target = "status",ignore = true)
    Order toEntity(OrderGetDto dto);

    @Mapping(target = "authUser",ignore = true)
    @Mapping(target = "good",ignore = true)
    @Mapping(target = "deliveryPoint",ignore = true)
    @Mapping(target = "status",ignore = true)
    Order toEntity(OrderUpdateDto dto);

    @Mapping(target = "authUser",ignore = true)
    @Mapping(target = "good",ignore = true)
    @Mapping(target = "deliveryPoint",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "id",ignore = true)
    Order toEntity(OrderCreateDto dto);

    @Mapping(target = "authUser",ignore = true)
    @Mapping(target = "good",ignore = true)
    @Mapping(target = "deliveryGetDto",ignore = true)
    @Mapping(target = "status",ignore = true)
    OrderGetDto toDto(Order order);

    default Page<OrderGetDto> toDto(Page<Order> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<OrderGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
