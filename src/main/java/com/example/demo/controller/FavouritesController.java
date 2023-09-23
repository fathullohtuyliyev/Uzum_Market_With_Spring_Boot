package com.example.demo.controller;

import com.example.demo.service.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FavouritesController {
    private final FavouritesService favouritesService;
}
