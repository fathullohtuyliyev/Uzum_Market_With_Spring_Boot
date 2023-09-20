package com.example.demo.dto.basket_dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BasketCreateDto {
    @NotNull
    public UUID userId;

    @NotNull
    public UUID goodId;
}
