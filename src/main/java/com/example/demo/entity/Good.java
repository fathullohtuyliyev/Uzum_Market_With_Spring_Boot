package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Entity(name = "good")
@Table(name = "good")
public class Good {
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
    @Column(nullable = false,name = "images")
    private List<String> images;

    @Column(name = "video_name")
    private String videoPath;

    @ManyToMany(mappedBy = "goods")
    private Set<Order> orders;

    @Builder.Default
    private boolean blocked=false;

    @ManyToMany(mappedBy = "goods")
    @ToString.Exclude
    private List<PromoCode> promoCodes;
}
