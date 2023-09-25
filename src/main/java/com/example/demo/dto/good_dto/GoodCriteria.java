package com.example.demo.dto.good_dto;

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
