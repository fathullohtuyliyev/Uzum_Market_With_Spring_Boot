package com.example.demo.dto.order_dto;

import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
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
    public UUID goodId;

    @NotNull
    @PositiveOrZero
    public Integer count;

    @NotNull
    @PositiveOrZero
    public Double price;

    @Builder.Default
    public LocalDateTime time=LocalDateTime.now();

    @NotNull
    public DeliveryGetDto deliveryGetDto;

    @NotBlank
    public String paymentType;
}
