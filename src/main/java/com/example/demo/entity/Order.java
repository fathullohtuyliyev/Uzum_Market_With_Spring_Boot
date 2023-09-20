package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "good_id")
    private Good good;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer count;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double price;

    @Builder.Default
    @Column(nullable = false,insertable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "delivery_point_id")
    private DeliveryPoint deliveryPoint;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    private LocalDateTime update;
}
