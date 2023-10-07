package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "basket")
@Table(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private AuthUser user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Product product;
}
