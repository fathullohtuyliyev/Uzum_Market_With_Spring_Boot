package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "payment_type")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false,unique = true)
    private String name;

    @Builder.Default
    private boolean active=true;

    @ToString.Exclude
    @OneToMany(mappedBy = "paymentType",fetch = FetchType.LAZY)
    private List<Order> orders;
}
