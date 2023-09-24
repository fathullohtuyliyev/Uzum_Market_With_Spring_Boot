package com.example.demo.dto.promo_code_dto;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.good_dto.GoodGetDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PromoCodeGetDto {
    public UUID id;

    public String name;

    public List<GoodGetDto> goods;
    public List<AuthUserGetDto> users;

    public Double discount;

    @Builder.Default
    public boolean active=true;
}
