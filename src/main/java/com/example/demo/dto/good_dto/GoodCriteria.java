package com.example.demo.dto.good_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GoodCriteria {
    @NotNull
    @Positive
    public Long color_id;

    @NotNull
    @Positive
    public Long type_id;

    @PositiveOrZero
    @NotNull
    public Double startPrice;

    @PositiveOrZero
    @NotNull
    public Double endPrice;
}
