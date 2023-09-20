package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.repository.AuthUserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.demo.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {
    private final AuthUserRepository authUserRepository;

    @Override
    public AuthUserGetDto save(AuthUserCreateDto dto) {
        try {
            if (authUserRepository.existsAuthUserByPhone(dto.phone)) {
                throw new ValidationException();
            }
            if (authUserRepository.existsAuthUserByEmail(dto.email)) {
                throw new ValidationException();
            }
            AuthUser authUser = USER_MAPPER.toEntity(dto);
            AuthUser save = authUserRepository.save(authUser);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(save);
            log.info("{} saved",dto1);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                            .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserGetDto update(AuthUserUpdateDto dto) {
        try {
            if (authUserRepository.existsAuthUserByPhone(dto.phone)) {
                throw new ValidationException();
            }
            if (authUserRepository.existsAuthUserByEmail(dto.email)) {
                throw new ValidationException();
            }
            authUserRepository.updateAuthUser(dto.phone,dto.firstName,dto.lastName,
                    dto.imagePath,dto.gender,dto.birthdate,dto.id);
            AuthUser authUser = authUserRepository.findAuthUserByIdAndBlockedFalse(dto.id);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(authUser);
            log.info("{} updated",dto1);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserGetDto get(UUID id) {
        try {
            AuthUser authUser = authUserRepository.findAuthUserByIdAndBlockedFalse(id);
            AuthUserGetDto dto = USER_MAPPER.toDto(authUser);
            log.info("{} gave",dto);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<AuthUserGetDto> users(Pageable pageable) {
        try {
            Page<AuthUser> all = authUserRepository.findAll(pageable);
            int allSize = authUserRepository.findAllSize();
            if (all.getContent().size()< allSize) {
                all = new PageImpl<>(authUserRepository.findAll(),
                        PageRequest.of(0,allSize),allSize);
            }
            List<UUID> uuidList = all.getContent()
                    .stream()
                    .map(AuthUser::getId)
                    .toList();
            log.info("{} gave users",uuidList);
            return USER_MAPPER.toDto(all);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
