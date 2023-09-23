package com.example.demo.controller;

import com.example.demo.dto.type_dto.TypeCreateDto;
import com.example.demo.dto.type_dto.TypeGetDto;
import com.example.demo.dto.type_dto.TypeUpdateDto;
import com.example.demo.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.type")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class TypeController {
    private final TypeService typeService;
    @PostMapping("/save")
    public ResponseEntity<TypeGetDto> save(@RequestBody  TypeCreateDto dto){
        TypeGetDto saved = typeService.save(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<TypeGetDto> update(@RequestBody TypeUpdateDto dto){
        TypeGetDto updated = typeService.update(dto);
        return new ResponseEntity<>(updated,HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-type/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TypeGetDto> getOnlyOneType(@PathVariable Long id){
        TypeGetDto getDto = typeService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @GetMapping("/get-many-types")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TypeGetDto>> getManyTypes(@RequestBody Collection<Long> ids){
        List<TypeGetDto> allByIds = typeService.getAllByIds(ids);
        return ResponseEntity.ok(allByIds);
    }
    @GetMapping("/get-all-types")
    public ResponseEntity<Page<TypeGetDto>> getAllTypes(@RequestParam String page,
                                                        @RequestParam String size){
        try {
            Page<TypeGetDto> types = typeService.types
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
            return ResponseEntity.ok(types);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

}
