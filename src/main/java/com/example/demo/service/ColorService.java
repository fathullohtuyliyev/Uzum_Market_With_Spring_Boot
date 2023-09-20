package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface ColorService {
    Map<Long,String> save(String name);
    Map<Long,String> update(Map<Long,String> colorDto);
    Map<Long,String> get(Long id);
    Page<Map<Long,String>> colors(Pageable pageable);
}
