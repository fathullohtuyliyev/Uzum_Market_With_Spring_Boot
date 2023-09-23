package com.example.demo.service;


import com.example.demo.dto.favourites_dto.FavouritesCreateDto;
import com.example.demo.dto.favourites_dto.FavouritesGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface FavouritesService {
    Page<FavouritesGetDto> save(FavouritesCreateDto dto);
    Page<FavouritesGetDto> delete(UUID goodId,UUID userId);
    Page<FavouritesGetDto> favourites(UUID userId, Pageable pageable);
}
