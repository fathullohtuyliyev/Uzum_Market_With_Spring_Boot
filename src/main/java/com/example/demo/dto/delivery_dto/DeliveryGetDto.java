package com.example.demo.dto.delivery_dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeliveryGetDto {
    public Long id;

    public String city;

    public String district;

    public String street;

    public String numberOfAddress;

    public boolean dressingRoom;

    public DeliveryAddressDto deliveryAddress;
}
