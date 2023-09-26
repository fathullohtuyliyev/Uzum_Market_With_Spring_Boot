package com.example.demo.controller;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.service.GoodService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.good")
@PreAuthorize("hasAuthority('SELLER')")
public class GoodController {
    private final GoodService goodService;
    @PostMapping("/save")
    public ResponseEntity<GoodGetDto> save(@RequestBody @Valid GoodCreateDto dto){
        return new ResponseEntity<>(goodService.save(dto), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @CachePut(key = "#dto.id",value = "goods")
    public ResponseEntity<GoodGetDto> update(@RequestBody @Valid GoodUpdateDto dto){
        return new ResponseEntity<>(goodService.update(dto),HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get")
    @Cacheable(key = "#id",value = "goods")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GoodGetDto> get(@RequestParam String id){
            return ResponseEntity.ok(goodService.get(UUID.fromString(id)));
    }

    @CacheEvict(key = "#id",value = "goods")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@RequestParam String id){
            goodService.delete(UUID.fromString(id));
            return ResponseEntity.noContent().build();
    }
    @GetMapping("/get-all")
    @Cacheable(key = "#root.methodName",value = "goods")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<GoodGetDto>> getAll(@RequestParam String page,
                                                   @RequestParam String size){
            Page<GoodGetDto> goodGetDtoList = goodService.find
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
            return ResponseEntity.ok(goodGetDtoList);
    }
    @GetMapping("/get-all-by-name")
    @Cacheable(key = "#param.get('name')",value = "goods")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<GoodGetDto>> getAllByName(@RequestParam Map<String, String> param){
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            String name = param.get("name");
            Page<GoodGetDto> goodGetDtoList = goodService.find
                    (PageRequest.of(page,size),name);
            return ResponseEntity.ok(goodGetDtoList);
    }
    @GetMapping("/get-all-by-criteria")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<GoodGetDto>> getAllByCriteria(@RequestParam String page,
                                                             @RequestParam String size,
                                                             @RequestBody @Valid GoodCriteria criteria){
            Page<GoodGetDto> goodGetDtoList = goodService.find
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)), criteria);
            return ResponseEntity.ok(goodGetDtoList);
    }
}
