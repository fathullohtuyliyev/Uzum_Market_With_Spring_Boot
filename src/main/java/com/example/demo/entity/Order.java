package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "order")
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_good", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "good_id"))
    private List<Good> goods;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer count;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double price;

    @Column(nullable = false,insertable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "delivery_point_id")
    private DeliveryPoint deliveryPoint;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "status_id")
    private Status status;

    private String promoCode;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @ToString.Exclude
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    private LocalDateTime update;
}
