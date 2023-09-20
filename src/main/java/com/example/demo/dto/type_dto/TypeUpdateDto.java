package com.example.demo.dto.type_dto;

import com.example.demo.dto.good_dto.GoodGetDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TypeUpdateDto {
    @Positive
    @NotNull
    public Long id;

    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Long root;

    @NotNull
    @Positive
    public Long sub;
}
