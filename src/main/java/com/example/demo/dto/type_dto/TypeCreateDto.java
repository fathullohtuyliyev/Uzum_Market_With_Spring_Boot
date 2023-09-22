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
public class TypeCreateDto extends TypeUpdateDto{
    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Long subId;

    @NotNull
    @Positive
    public Set<Long> rootsId;
}
