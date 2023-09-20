package com.example.demo.dto.order_dto;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.good_dto.GoodGetDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderGetDto {
    public UUID id;

    public AuthUserGetDto authUser;

    public GoodGetDto good;

    public Integer count;

    public Double price;

    public LocalDateTime time=LocalDateTime.now();

    public DeliveryGetDto deliveryGetDto;

    public Map<Integer,String> status;

    public LocalDateTime update;

    public Map<String, Boolean> paymentType;
}
