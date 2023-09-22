package com.example.demo.entity;

import com.example.demo.entity.Good;
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
@Entity(name = "type")
@Table(name = "type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false,unique = true)
    private String name;

    @ManyToOne
    @ToString.Exclude
    private Type sub;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<Type> roots;
}
