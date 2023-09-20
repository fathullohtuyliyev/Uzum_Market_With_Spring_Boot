package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.List;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "promo_code")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false,unique = true,updatable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "authuser_promocode",
            joinColumns = @JoinColumn(name = "promocode_id"),
            inverseJoinColumns = @JoinColumn(name = "authuser_id"))
    private List<AuthUser> authUsers;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "good_promocode",
            joinColumns = @JoinColumn(name = "promocode_id"),
            inverseJoinColumns = @JoinColumn(name = "good_id"))
    private List<Good> goods;

    @NotNull
    @Positive
    private Double discount;

    @Builder.Default
    private boolean active=true;
}
