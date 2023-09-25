package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "user_data")
@Table(name = "user_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true,name = "user_id", referencedColumnName = "id")
    private AuthUser user;

    @NotNull
    @Column(nullable = false)
    private String data;

    @Column(name = "expire_time",nullable = false)
    private LocalDateTime expireTime;
}
