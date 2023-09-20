package com.example.demo.nosql;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Images {
    @Id
    private UUID id;

    @NotNull
    private UUID goodId;

    @NotNull
    private List<String> paths;
}
