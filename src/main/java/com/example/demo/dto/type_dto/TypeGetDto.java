package com.example.demo.dto.type_dto;

import com.example.demo.dto.good_dto.GoodGetDto;
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

    public Long root;

    public Long sub;

    public List<GoodGetDto> goods;
}
