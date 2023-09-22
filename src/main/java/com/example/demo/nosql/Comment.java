package com.example.demo.nosql;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Comment {
    @Id
    private UUID id;

    @NotNull
    private UUID goodId;

    private List<String> images;

    private Set<UUID> spammedUsers;

    @Min(1)
    @NotNull
    @Max(5)
    private Short rating;

    @NotNull
    private String message;

    @NotNull
    private UUID userId;

    private LocalDateTime commentDate;

    private String response;

    private LocalDateTime responseDate;
}
