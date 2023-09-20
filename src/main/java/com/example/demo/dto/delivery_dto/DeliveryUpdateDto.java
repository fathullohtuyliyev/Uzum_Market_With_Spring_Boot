package com.example.demo.dto.delivery_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeliveryUpdateDto {
    @Positive
    @NotNull
    public Long id;

    @NotBlank
    public String city;

    @NotBlank
    public String district;

    @NotBlank
    public String street;

    @NotBlank
    public String numberOfAddress;

    @Builder.Default
    public boolean dressingRoom=true;

    @NotNull
    public DeliveryAddressDto deliveryAddress;
}
