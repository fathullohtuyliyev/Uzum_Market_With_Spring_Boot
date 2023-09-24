package com.example.demo.dto.auth_user_dto;

import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.PromoCode;
import com.example.demo.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AuthUserGetDto {
    public UUID id;

    public String firstName;

    public String lastName;

    public String fatherName;

    public String phone;

    public LocalDate birthdate;

    public Gender gender;

    public List<String> roles;

    public boolean online;

    public String images;

    public boolean active;

    public String email;
}
