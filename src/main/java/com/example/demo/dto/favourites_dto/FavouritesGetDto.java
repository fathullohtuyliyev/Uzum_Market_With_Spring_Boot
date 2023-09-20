package com.example.demo.dto.favourites_dto;

import com.example.demo.dto.good_dto.GoodGetDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FavouritesGetDto {
    public Long id;

    public UUID userId;

    public UUID goodId;
}
