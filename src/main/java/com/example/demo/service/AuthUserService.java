package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public interface AuthUserService {
    AuthUserGetDto save(AuthUserCreateDto dto);
    AuthUserGetDto update(AuthUserUpdateDto dto);
    AuthUserGetDto get(UUID id);
    Page<AuthUserGetDto> users(Pageable pageable);
}
