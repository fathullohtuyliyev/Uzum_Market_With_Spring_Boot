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
@Entity(name = "delivery_point")
public class DeliveryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false)
    private String district;

    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotBlank
    @Column(nullable = false,name = "number_of_address")
    private String numberOfAddress;

    @Builder.Default
    @Column(nullable = false,name = "dressing_room")
    private boolean dressingRoom=true;

    @Column(name = "delivery_address",unique = true,nullable = false)
    private double[] deliveryAddress;

    @ToString.Exclude
    @OneToMany(mappedBy = "deliveryPoint")
    private List<Order> orders;
}
