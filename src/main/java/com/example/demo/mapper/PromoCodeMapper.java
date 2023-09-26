package com.example.demo.mapper;

import com.example.demo.dto.promo_code_dto.PromoCodeCreateDto;
import com.example.demo.dto.promo_code_dto.PromoCodeGetDto;
import com.example.demo.dto.promo_code_dto.PromoCodeUpdateDto;
import com.example.demo.entity.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

@Mapper
public interface PromoCodeMapper {
    PromoCodeMapper PROMO_CODE_MAPPER = Mappers.getMapper(PromoCodeMapper.class);
    @Mapping(target = "authUsers",ignore = true)
    @Mapping(target = "goods",ignore = true)
    @Mapping(target = "id",ignore = true)
    PromoCode toEntity(PromoCodeCreateDto dto);

    @Mapping(target = "authUsers",ignore = true)
    @Mapping(target = "goods",ignore = true)
    PromoCode toEntity(PromoCodeGetDto dto);

    @Mapping(target = "authUsers",ignore = true)
    @Mapping(target = "goods",ignore = true)
    PromoCode toEntity(PromoCodeUpdateDto dto);

    @Mapping(target = "users",ignore = true)
    @Mapping(target = "goods",ignore = true)
    PromoCodeGetDto toDto(PromoCode promoCode);

    default Page<PromoCodeGetDto> toDto(Page<PromoCode> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<PromoCodeGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
