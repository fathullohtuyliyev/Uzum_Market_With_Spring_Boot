package com.example.demo.controller;

import com.example.demo.dto.basket_dto.BasketCreateDto;
import com.example.demo.dto.basket_dto.BasketGetDto;
import com.example.demo.service.BasketService;
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
@RequestMapping("/api.basket")
@PreAuthorize("isAuthenticated()")
public class BasketController {
    private final BasketService basketService;
    @PostMapping("/save")
    public ResponseEntity<Page<BasketGetDto>> save(@RequestBody @Valid BasketCreateDto dto){
        return new ResponseEntity<>(basketService.save(dto), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Page<BasketGetDto>> delete(@RequestParam String userId,
                                                         @RequestParam String goodId){
            return new ResponseEntity<>(basketService.delete
                    (UUID.fromString(goodId), UUID.fromString(userId)), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get")
    @Cacheable(key = "#param.get('id')",value = "baskets")
    public ResponseEntity<Page<BasketGetDto>> get(@RequestParam Map<String, String> param){
            UUID userId = UUID.fromString(param.get("id"));
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            Page<BasketGetDto> basket = basketService.get(userId, PageRequest.of(page, size));
            return ResponseEntity.ok(basket);
    }
}
