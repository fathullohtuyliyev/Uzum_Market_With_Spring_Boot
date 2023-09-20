package com.example.demo.dto.auth_user_dto;

import com.example.demo.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AuthUserCreateDto {
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

    @NotBlank
    @Email
    public String email;
}
