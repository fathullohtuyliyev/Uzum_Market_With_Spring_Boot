package com.example.demo.controller;

import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import com.example.demo.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.delivery")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class DeliveryController {
    private final DeliveryService deliveryService;
    @PostMapping("/save")
    public ResponseEntity<DeliveryGetDto> save(@RequestBody DeliveryCreateDto dto){
        return new ResponseEntity<>(deliveryService.save(dto), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<DeliveryGetDto> update(@RequestBody DeliveryUpdateDto dto){
        return new ResponseEntity<>(deliveryService.update(dto),HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<DeliveryGetDto> get(@PathVariable Long id){
        return ResponseEntity.ok(deliveryService.get(id));
    }
    @GetMapping("/get")
    public ResponseEntity<Page<DeliveryGetDto>> getAll(@RequestParam String page,
                                                       @RequestParam String size){
        try {
            Page<DeliveryGetDto> deliveryGetDtoList = deliveryService.deliveryPoints
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
            return ResponseEntity.ok(deliveryGetDtoList);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
