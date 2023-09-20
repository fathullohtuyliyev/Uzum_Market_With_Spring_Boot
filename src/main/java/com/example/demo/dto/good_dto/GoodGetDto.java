package com.example.demo.dto.good_dto;

import com.example.demo.nosql.Comments;
import lombok.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GoodGetDto {
    public UUID id;

    public Map<Long,String> color;

    public Map<Long,String> type;

    public String name;

    public String description;

    public Double price;

    public Integer count;

    @Builder.Default
    public Double discountPrice=0d;

    public UUID images;

    public Set<Comments> comments;

    public Integer ordersCount;
}
