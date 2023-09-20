package com.example.demo.dto.type_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TypeCreateDto {
    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Long root;

    @NotNull
    @Positive
    public Long sub;
}
