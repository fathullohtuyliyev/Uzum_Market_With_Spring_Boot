package com.example.demo.nosql;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Comments {
    @Id
    private UUID id;

    @NotNull
    private UUID goodId;

    private Images images;

    private boolean spam;

    @Min(1)
    @NotNull
    @Max(5)
    private Short rating;

    @NotNull
    private String message;

    @NotNull
    private UUID userId;

    @NotNull
    @Builder.Default
    private LocalDateTime commentDate=LocalDateTime.now();

    private String response;

    private LocalDateTime responseDate;
}
