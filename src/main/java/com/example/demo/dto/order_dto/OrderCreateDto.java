package com.example.demo.dto.order_dto;

import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.entity.PromoCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderCreateDto {
    @NotNull
    public UUID authUserId;

    @NotNull
    public List<UUID> goodsId;

    public Integer count;

    private String promoCode;

    @Builder.Default
    public LocalDateTime time=LocalDateTime.now();

    @NotNull
    public DeliveryGetDto deliveryGetDto;

    @NotBlank
    public String paymentType;
}
