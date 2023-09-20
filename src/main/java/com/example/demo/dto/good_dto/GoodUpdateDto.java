package com.example.demo.dto.good_dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GoodUpdateDto extends GoodCreateDto{
    public UUID id;

    @Positive
    @NotNull
    public Long color_id;

    @NotNull
    @Positive
    public Long type_id;

    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotNull
    @PositiveOrZero
    public Double price;

    @NotNull
    @PositiveOrZero
    public Integer count;

    @NotNull
    @PositiveOrZero
    public Integer orders;

    @Builder.Default
    public Double discountPrice=0d;

    @NotNull
    public UUID imagesId;

    @Column(name = "comments_id")
    public UUID commentsId;
}
