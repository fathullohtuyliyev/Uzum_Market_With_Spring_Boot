package com.example.demo.controller;

import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import com.example.demo.service.OrderService;
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

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.order")
@PreAuthorize("hasAuthority('SELLER')")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderGetDto> save(@RequestBody @Valid OrderCreateDto dto){
        return new ResponseEntity<>(orderService.save(dto), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @CachePut(key = "#dto.id",value = "orders")
    @PreAuthorize("hasAnyAuthority('SELLER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<OrderGetDto> update(@RequestBody @Valid OrderUpdateDto dto){
        return new ResponseEntity<>(orderService.update(dto),HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update-status")
    @CachePut(key = "#id",value = "orders")
    public ResponseEntity<OrderGetDto> updateStatus(@RequestParam String id,
                                                    @RequestParam String name){
        try {
            return new ResponseEntity<>(orderService.updateStatus
                    (UUID.fromString(id), name), HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/get")
    @Cacheable(key = "#param.get('userId')",value = "orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrderGetDto>> get(@RequestParam Map<String, String> param){
        try {
            UUID userId = UUID.fromString(param.get("userId"));
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            Page<OrderGetDto> orders = orderService.orders
                    (userId, PageRequest.of(page, size));
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
