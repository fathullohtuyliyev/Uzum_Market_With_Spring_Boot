package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface AuthUserService {
    void save(AuthUserCreateDto dto, HttpServletResponse response);
    void activate(String code, HttpServletRequest request);
    void checkAndSendPasswordToEmail(String email, HttpServletResponse response);
    AuthUserGetDto login(String password, HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    AuthUserGetDto update(AuthUserUpdateDto dto);
    AuthUserGetDto get(UUID id, HttpServletRequest request);
    Page<AuthUserGetDto> users(Pageable pageable);
    void online(UUID userId);
    void offline(UUID userId);
    boolean existEmail(String email);
    boolean existPhone(String phone);
}
