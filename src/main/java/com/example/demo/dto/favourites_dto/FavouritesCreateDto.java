package com.example.demo.dto.favourites_dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FavouritesCreateDto {
    @NotNull
    public UUID userId;

    @NotNull
    public UUID goodId;
}
