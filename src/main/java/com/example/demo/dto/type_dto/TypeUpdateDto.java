package com.example.demo.dto.type_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TypeUpdateDto{
    @Positive
    @NotNull
    public Long id;

    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Long subId;

    @NotNull
    @Positive
    public Long rootId;
}
