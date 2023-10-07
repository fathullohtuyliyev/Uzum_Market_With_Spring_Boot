package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.List;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "product")
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "color_id")
    @ToString.Exclude
    private Color color;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "type_id")
    @ToString.Exclude
    private Type type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @PositiveOrZero
    private Double price;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer ordersCount;

    @Builder.Default
    @PositiveOrZero
    private Double discountPrice=0d;

    @NotNull
    @ElementCollection
    @ToString.Exclude
    @Column(nullable = false,name = "images")
    private List<String> images;

    @Column(name = "video_name")
    private String videoPath;

    @ToString.Exclude
    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;

    @Builder.Default
    private boolean blocked=false;

    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    private List<PromoCode> promoCodes;
}
