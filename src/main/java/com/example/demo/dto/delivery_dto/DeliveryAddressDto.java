package com.example.demo.dto.delivery_dto;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class DeliveryAddressDto {
    @NonNull
    public Long id;

    @NonNull
    public Double lat;

    @NonNull
    public Double lng;
}
