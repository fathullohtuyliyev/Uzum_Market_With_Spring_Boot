package com.example.demo.dto.order_dto;

import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderUpdateDto {
    @NotNull
    public UUID id;

    @NotNull
    public DeliveryGetDto deliveryGetDto;


    @NotNull
    @Builder.Default
    public LocalDateTime update=LocalDateTime.now();

    @NotBlank
    public String paymentType;
}
