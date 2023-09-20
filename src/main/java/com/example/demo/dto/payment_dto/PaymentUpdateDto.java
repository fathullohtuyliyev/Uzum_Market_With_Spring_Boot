package com.example.demo.dto.payment_dto;

import jakarta.persistence.Column;
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
public class PaymentUpdateDto {
    @Positive
    @NotNull
    public Integer id;

    @NotBlank
    public String name;

    @Builder.Default
    public boolean active=true;
}
