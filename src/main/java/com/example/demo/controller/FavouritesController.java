package com.example.demo.controller;

import com.example.demo.dto.favourites_dto.FavouritesCreateDto;
import com.example.demo.dto.favourites_dto.FavouritesGetDto;
import com.example.demo.service.FavouritesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.favourites")
@PreAuthorize("isAuthenticated()")
public class FavouritesController {
    private final FavouritesService favouritesService;
    @PostMapping("/save")
    @Cacheable(key = "#dto.userId",value = "favourites")
    public ResponseEntity<Page<FavouritesGetDto>> save(@RequestBody @Valid FavouritesCreateDto dto){
        return new ResponseEntity<>(favouritesService.save(dto), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Page<FavouritesGetDto>> delete(@RequestParam String userId,
                                                         @RequestParam String goodId){
            return new ResponseEntity<>(favouritesService.delete
                    (UUID.fromString(goodId), UUID.fromString(userId)), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get")
    public ResponseEntity<Page<FavouritesGetDto>> get(@RequestParam Map<String, String> param){
            UUID userId = UUID.fromString(param.get("id"));
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            Page<FavouritesGetDto> favourites = favouritesService.favourites(userId, PageRequest.of(page, size));
            return ResponseEntity.ok(favourites);
    }
}
