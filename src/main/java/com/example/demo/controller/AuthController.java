package com.example.demo.controller;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.exception.BadParamException;
import com.example.demo.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.auth")
@PreAuthorize("permitAll()")
public class AuthController {
    private final AuthUserService authUserService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthUserCreateDto dto, HttpServletResponse response){
        authUserService.save(dto, response);
        return new ResponseEntity<>("",HttpStatus.CREATED);
    }
    @PutMapping("/activation")
    public ResponseEntity<Void> activation(@RequestParam String code, HttpServletRequest request){
        authUserService.activate(code, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login-1")
    public void checkAndSentPasswordToEmail(@RequestParam String email, HttpServletResponse response){
        authUserService.checkAndSendPasswordToEmail(email,response);
    }
    @GetMapping("/login-2")
    public ResponseEntity<AuthUserGetDto> login(@RequestParam String password,
                                                HttpServletRequest request,
                                                HttpServletResponse response){
        AuthUserGetDto dto = authUserService.login(password, request, response);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        authUserService.logout(request, response);
    }
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    @CachePut(key = "#dto.id",value = "users")
    public ResponseEntity<AuthUserGetDto> update(@RequestBody @Valid AuthUserUpdateDto dto){
        AuthUserGetDto update = authUserService.update(dto);
        return new ResponseEntity<>(update, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-self-data")
    @PreAuthorize("isAuthenticated()")
    @Cacheable(key = "#id",value = "users")
    public ResponseEntity<AuthUserGetDto> get(@RequestParam String id, HttpServletRequest request){
        try {
            AuthUserGetDto getDto = authUserService.get(UUID.fromString(id), request);
            return ResponseEntity.ok(getDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/online")
    @PreAuthorize("isAuthenticated()")
    public void online(@RequestParam String id){
        try {
            authUserService.online(UUID.fromString(id));
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }
    }
    @PutMapping("/offline")
    @PreAuthorize("isAuthenticated()")
    public void offline(@RequestParam String id){
        try {
            authUserService.offline(UUID.fromString(id));
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }
    }
    @GetMapping("/exist-email/{email}")
    public ResponseEntity<Boolean> existEmail(@PathVariable String email){
        return ResponseEntity.ok(authUserService.existEmail(email));
    }
    @GetMapping("/exist-phone/{phone}")
    public ResponseEntity<Boolean> existPhone(@PathVariable String phone){
        return ResponseEntity.ok(authUserService.existPhone(phone));
    }
}
