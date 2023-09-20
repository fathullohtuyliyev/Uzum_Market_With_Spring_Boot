package com.example.demo.dto.delivery_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeliveryCreateDto {
    @NotBlank
    public String city;

    @NotBlank
    public String district;

    @NotBlank
    public String street;

    @NotBlank
    public String numberOfAddress;

    @NotNull
    public DeliveryAddressDto deliveryAddress;
}
