package com.example.demo.dto.basket_dto;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Good;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BasketGetDto {
    public UUID id;

    public AuthUserGetDto user;

    public GoodGetDto good;
}
