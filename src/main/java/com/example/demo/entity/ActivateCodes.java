package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "activate_codes")
@Table(name = "activate_codes")
public class ActivateCodes {
    @Id
    @Builder.Default
    private Integer code=new Random().nextInt(100000,999999);

    @ManyToOne(optional = false,cascade = CascadeType.ALL
            ,fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;

    @NotNull
    @Builder.Default
    @Future
    private LocalDateTime valid = LocalDateTime.now().plusMinutes(10);
}

