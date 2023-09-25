package com.example.demo.entity;

import com.example.demo.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "auth_user")
@Table(name = "auth_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String fatherName;

    @Column(nullable = false,unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "auth_user_role",
            joinColumns = @JoinColumn(name = "authuser_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    private boolean online;


    private String images;

    @Builder.Default
    private boolean active=false;

    @Column(nullable = false,unique = true)
    private String email;

    @ManyToMany(mappedBy = "authUsers")
    @ToString.Exclude
    private List<PromoCode> promoCodes;

    @ToString.Exclude
    @OneToMany(mappedBy = "authUser")
    private List<Order> orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "authUser")
    private List<ActivateCodes> activateCodes;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_id", referencedColumnName = "id")
    private UserData data;

    private String temporaryPassword;
}
