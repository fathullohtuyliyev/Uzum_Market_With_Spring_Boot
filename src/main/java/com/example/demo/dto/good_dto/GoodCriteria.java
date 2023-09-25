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
    public Long color_id;

    public Long type_id;

    public Double startPrice;

    public Double endPrice;
}
