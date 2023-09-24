package com.example.demo.controller;

import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import com.example.demo.service.PaymentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api.payment.type")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;
    @PostMapping("/save/{name}")
    public ResponseEntity<PaymentGetDto> save(@PathVariable String name){
        return new ResponseEntity<>(paymentTypeService.save(name), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @CachePut(key = "#dto.id",value = "paymentTypes")
    public ResponseEntity<PaymentGetDto> update(@RequestBody @Valid PaymentUpdateDto dto){
        return new ResponseEntity<>(paymentTypeService.update(dto),HttpStatus.NO_CONTENT);
    }
    @CacheEvict(key = "#id",value = "paymentTypes")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        paymentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Cacheable(key = "#id",value = "paymentTypes")
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentGetDto> get(@PathVariable Integer id){
        return ResponseEntity.ok(paymentTypeService.get(id));
    }
    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PaymentGetDto>> getAll(@RequestParam String page,
                                                      @RequestParam String size){
        try {
            return ResponseEntity.ok(paymentTypeService
                    .paymentTypes(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size))));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
