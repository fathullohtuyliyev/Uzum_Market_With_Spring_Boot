package com.example.demo.controller;

import com.example.demo.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SELLER','ADMIN','SUPER_ADMIN')")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
}
