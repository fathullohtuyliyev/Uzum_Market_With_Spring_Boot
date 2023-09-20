package com.example.demo.service;

import com.example.demo.dto.favourites_dto.FavouritesCreateDto;
import com.example.demo.dto.favourites_dto.FavouritesGetDto;
import com.example.demo.entity.Favourites;
import com.example.demo.repository.FavouritesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {
    private final FavouritesRepository favouritesRepository;

    @Override
    public Page<FavouritesGetDto> save(FavouritesCreateDto dto) {
        try {
            favouritesRepository.saveToFavourites(dto.userId,dto.goodId);
            Page<Favourites> all = favouritesRepository.findAllByUserId
                    (dto.userId, PageRequest.of(0,15));
            int sizeByUserId = favouritesRepository.findSizeByUserId(dto.userId);
            if (all.getContent().size()> sizeByUserId) {
                all = new PageImpl<>(favouritesRepository.findAllByUserId(dto.userId),
                        PageRequest.of(0,sizeByUserId),sizeByUserId);
            }
            List<FavouritesGetDto> list = all.getContent()
                    .stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .goodId(favourites.getGood().getId())
                            .userId(favourites.getUser().getId())
                            .build()
                    ).toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<FavouritesGetDto> delete(UUID goodId, UUID userId) {
        try {
            favouritesRepository.removeFromFavourites(userId,goodId);
            Page<Favourites> allByUserId = favouritesRepository.findAllByUserId(userId, PageRequest.of(0, 15));
            int sizeByUserId = favouritesRepository.findSizeByUserId(userId);
            if (allByUserId.getPageable().getPageSize()< sizeByUserId) {
                allByUserId = new PageImpl<>(allByUserId.getContent(),
                        allByUserId.getPageable(),sizeByUserId);
            }
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getGood().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<FavouritesGetDto> users(UUID userId, Pageable pageable) {
        try {
            Page<Favourites> allByUserId = favouritesRepository.findAllByUserId(userId, pageable);
            int sizeByUserId = favouritesRepository.findSizeByUserId(userId);
            if (allByUserId.getPageable().getPageSize()< sizeByUserId) {
                allByUserId = new PageImpl<>(favouritesRepository.findAllByUserId(userId),
                        PageRequest.of(0,15),sizeByUserId);
            }
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getGood().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
