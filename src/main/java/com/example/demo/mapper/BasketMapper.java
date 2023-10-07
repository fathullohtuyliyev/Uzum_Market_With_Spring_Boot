package com.example.demo.mapper;

import com.example.demo.dto.basket_dto.BasketGetDto;
import com.example.demo.entity.Basket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

@Mapper
public interface BasketMapper {
    BasketMapper BASKET_MAPPER = Mappers.getMapper(BasketMapper.class);
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "product",ignore = true)
    Basket toEntity(BasketGetDto dto);

    @Mapping(target = "user",ignore = true)
    @Mapping(target = "good",ignore = true)
    BasketGetDto toDto(Basket basket);

    default Page<BasketGetDto> toDto(Page<Basket> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<BasketGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
