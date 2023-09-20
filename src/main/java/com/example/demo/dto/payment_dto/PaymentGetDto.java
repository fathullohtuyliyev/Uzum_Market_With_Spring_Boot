package com.example.demo.dto.payment_dto;

import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.entity.Order;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PaymentGetDto {
    public Integer id;

    public String name;

    @Builder.Default
    public boolean active=true;

    public List<OrderGetDto> orders;
}
