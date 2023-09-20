package com.example.demo.dto.promo_code_dto;

import com.example.demo.dto.good_dto.GoodGetDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PromoCodeUpdateDto extends PromoCodeCreateDto{
    @NotNull
    public UUID id;

    @NotBlank
    public String name;


    @NotNull
    public List<GoodGetDto> goods;

    @NotNull
    @Positive
    public Double discount;

    @Builder.Default
    public boolean active=true;
}
