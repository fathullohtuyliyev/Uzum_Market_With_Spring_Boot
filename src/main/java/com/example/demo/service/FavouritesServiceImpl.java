package com.example.demo.service;

import com.example.demo.dto.favourites_dto.FavouritesCreateDto;
import com.example.demo.dto.favourites_dto.FavouritesGetDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Favourites;
import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.FavouritesRepository;
import com.example.demo.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {
    private final GoodRepository goodRepository;
    private final AuthUserRepository authUserRepository;
    private final FavouritesRepository favouritesRepository;

    @Override
    public Page<FavouritesGetDto> save(FavouritesCreateDto dto) {
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(dto.userId)
                    .orElseThrow(NotFoundException::new);
            Product product = goodRepository.findByIdAndBlockedFalse(dto.goodId)
                    .orElseThrow(NotFoundException::new);
            Favourites favourites = Favourites.builder()
                    .user(authUser)
                    .product(product)
                    .build();
            favouritesRepository.save(favourites);
            Page<Favourites> all = favouritesRepository.findAllByUserId
                    (dto.userId, PageRequest.of(0,15));
            List<FavouritesGetDto> list = all.getContent()
                    .stream()
                    .map(favouritesLambda -> FavouritesGetDto.builder()
                            .goodId(favouritesLambda.getProduct().getId())
                            .userId(favouritesLambda.getUser().getId())
                            .build()
                    ).toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
    }

    @Override
    public Page<FavouritesGetDto> delete(UUID goodId, UUID userId) {
            favouritesRepository.removeFromFavourites(userId,goodId);
            Page<Favourites> allByUserId = favouritesRepository.findAllByUserId(userId, PageRequest.of(0, 15));
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getProduct().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
    }

    @Override
    public Page<FavouritesGetDto> favourites(UUID userId, Pageable pageable) {
            Page<Favourites> allByUserId = favouritesRepository.findAllByUserId(userId, pageable);
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getProduct().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
    }
}
