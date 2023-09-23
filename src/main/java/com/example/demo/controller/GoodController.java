package com.example.demo.controller;

import com.example.demo.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class GoodController {
    private final GoodService goodService;
}
