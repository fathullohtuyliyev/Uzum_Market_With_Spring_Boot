package com.example.demo.mapper;

import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import com.example.demo.entity.PaymentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

@Mapper
public interface PaymentMapper {
    PaymentMapper PAYMENT_MAPPER = Mappers.getMapper(PaymentMapper.class);
    @Mapping(target = "orders",ignore = true)
    PaymentType toEntity(PaymentGetDto dto);

    @Mapping(target = "orders",ignore = true)
    PaymentType toEntity(PaymentUpdateDto dto);

    PaymentGetDto toDto(PaymentType paymentType);

    default Page<PaymentGetDto> toDto(Page<PaymentType> all){
        if (all==null || all.isEmpty()) {
            return Page.empty();
        }
        List<PaymentGetDto> list = all.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, all.getPageable(), list.size());
    }
}
