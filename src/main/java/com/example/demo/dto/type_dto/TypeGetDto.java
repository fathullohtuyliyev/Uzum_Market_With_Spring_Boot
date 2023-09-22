package com.example.demo.dto.type_dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TypeGetDto {

    public Long id;

    public String name;

    public Long subId;

    public List<TypeGetDto> roots;
}
