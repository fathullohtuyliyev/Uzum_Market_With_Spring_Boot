package com.example.demo.service;

import com.example.demo.dto.favourites_dto.FavouritesCreateDto;
import com.example.demo.dto.favourites_dto.FavouritesGetDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Favourites;
import com.example.demo.entity.Good;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
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
import java.util.Arrays;
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
        try {
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(dto.userId)
                    .orElseThrow(NotFoundException::new);
            Good good = goodRepository.findByIdAndBlockedFalse(dto.goodId)
                    .orElseThrow(NotFoundException::new);
            Favourites favourites = Favourites.builder()
                    .user(authUser)
                    .good(good)
                    .build();
            favouritesRepository.save(favourites);
            Page<Favourites> all = favouritesRepository.findAllByUserId
                    (dto.userId, PageRequest.of(0,15));
            int sizeByUserId = favouritesRepository.findSizeByUserId(dto.userId);
            if (all.getContent().size()> sizeByUserId) {
//                all = new PageImpl<>(favouritesRepository.findAllByUserId(dto.userId),
//                        PageRequest.of(0,sizeByUserId),sizeByUserId);
            }
            List<FavouritesGetDto> list = all.getContent()
                    .stream()
                    .map(favouritesLambda -> FavouritesGetDto.builder()
                            .goodId(favouritesLambda.getGood().getId())
                            .userId(favouritesLambda.getUser().getId())
                            .build()
                    ).toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
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
//                allByUserId = new PageImpl<>(allByUserId.getContent(),
//                        allByUserId.getPageable(),sizeByUserId);
            }
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getGood().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<FavouritesGetDto> favourites(UUID userId, Pageable pageable) {
        try {
            Page<Favourites> allByUserId = favouritesRepository.findAllByUserId(userId, pageable);
            int sizeByUserId = favouritesRepository.findSizeByUserId(userId);
            if (allByUserId.getPageable().getPageSize()< sizeByUserId) {
//                allByUserId = new PageImpl<>(favouritesRepository.findAllByUserId(userId),
//                        PageRequest.of(0,15),sizeByUserId);
            }
            List<FavouritesGetDto> list = allByUserId.stream()
                    .map(favourites -> FavouritesGetDto.builder()
                            .userId(favourites.getUser().getId())
                            .goodId(favourites.getGood().getId())
                            .build())
                    .toList();
            return new PageImpl<>(list,allByUserId.getPageable(),list.size());
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
