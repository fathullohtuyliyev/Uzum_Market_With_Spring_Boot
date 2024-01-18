package com.example.demo.controller;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.service.AuthUserService;
import com.example.demo.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        authUserService.logout(request, response);
    }
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    @CachePut(key = "#dto.id",value = "users")
    public ResponseEntity<AuthUserGetDto> update(@RequestBody @Valid AuthUserUpdateDto dto){
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!dto.getId().equals(userDetails.authUser().getId())) {
            throw new ForbiddenAccessException();
        }
        AuthUserGetDto update = authUserService.update(dto);
        return new ResponseEntity<>(update, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-self-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthUserGetDto> get(HttpServletRequest request){
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AuthUserGetDto getDto = authUserService.get(userDetails.authUser().getId());
            return ResponseEntity.ok(getDto);
    }
    @PutMapping("/online")
    @PreAuthorize("isAuthenticated()")
    public void online(@RequestParam String id){
            authUserService.online(UUID.fromString(id));
    }
    @PutMapping("/offline")
    @PreAuthorize("isAuthenticated()")
    public void offline(@RequestParam String id){
            authUserService.offline(UUID.fromString(id));
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
