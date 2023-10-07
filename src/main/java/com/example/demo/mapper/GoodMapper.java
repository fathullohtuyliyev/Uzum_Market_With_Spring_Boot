package com.example.demo.mapper;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

@Mapper
public interface GoodMapper {
    GoodMapper GOOD_MAPPER = Mappers.getMapper(GoodMapper.class);

    @Mapping(target = "color",ignore = true)
    @Mapping(target = "type",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "blocked",ignore = true)
    @Mapping(target = "promoCodes",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "ordersCount",ignore = true)
    Product toEntity(GoodCreateDto dto);

    @Mapping(target = "color",ignore = true)
    @Mapping(target = "type",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "blocked",ignore = true)
    @Mapping(target = "promoCodes",ignore = true)
    Product toEntity(GoodUpdateDto dto);

    @Mapping(target = "color",ignore = true)
    @Mapping(target = "type",ignore = true)
    @Mapping(target = "orders",ignore = true)
    @Mapping(target = "blocked",ignore = true)
    @Mapping(target = "promoCodes",ignore = true)
    Product toEntity(GoodGetDto dto);

    @Mapping(target = "color",ignore = true)
    @Mapping(target = "type",ignore = true)
    GoodGetDto toDto(Product product);

    default Page<GoodGetDto> toDto(Page<Product> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<GoodGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
