package com.example.demo.dto.auth_user_dto;

import com.example.demo.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AuthUserUpdateDto {
    @NotBlank
    public UUID id;

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    public String fatherName;

    @Pattern(regexp = "^\\+[0-9]{10,13}$")
    @NotBlank
    public String phone;

    @NotNull
    public Gender gender;

    @NotNull
    @Past
    public LocalDate birthdate;

    public String imagePath;

    @NotBlank
    @Email
    public String email;
}
