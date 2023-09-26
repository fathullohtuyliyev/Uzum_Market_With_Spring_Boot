package com.example.demo.service;

import com.example.demo.dto.basket_dto.BasketCreateDto;
import com.example.demo.dto.basket_dto.BasketGetDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Basket;
import com.example.demo.entity.Good;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.BasketRepository;
import com.example.demo.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.demo.mapper.BasketMapper.BASKET_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final GoodRepository goodRepository;
    private final AuthUserRepository authUserRepository;
    private final BasketRepository basketRepository;

    @Override
    public Page<BasketGetDto> save(BasketCreateDto dto) {
            checkMethod(dto.userId,authUserRepository);
            Good good = goodRepository.findById(dto.goodId)
                    .orElseThrow(NotFoundException::new);
            AuthUser authUser = authUserRepository.findById(dto.userId)
                    .orElseThrow(NotFoundException::new);
            Basket basket = Basket.builder()
                    .user(authUser)
                    .good(good)
                    .build();
            basketRepository.save(basket);
            Page<Basket> baskets = basketRepository.findAllByUserId(authUser, PageRequest.of(0,20));
            return BASKET_MAPPER.toDto(baskets);
    }

    @Override
    public Page<BasketGetDto> delete(UUID goodId,UUID userId) {
            checkMethod(userId,authUserRepository);
            basketRepository.deleteByGoodIdAndUserId(goodId,userId);
            AuthUser authUser = authUserRepository.findById(userId)
                    .orElseThrow(NotFoundException::new);
            Page<Basket> allByUserId = basketRepository.findAllByUserId(authUser, PageRequest.of(0, 20));
            return BASKET_MAPPER.toDto(allByUserId);
    }

    private static void checkMethod(UUID userId,AuthUserRepository authUserRepository){
        if(!authUserRepository.isActive(userId)){
            throw new ForbiddenAccessException();
        }
    }
    @Override
    public Page<BasketGetDto> get(UUID userId,Pageable pageable) {
            checkMethod(userId,authUserRepository);
            AuthUser authUser = authUserRepository.findById(userId)
                    .orElseThrow(NotFoundException::new);
            Page<Basket> allByUserId = basketRepository.findAllByUserId(authUser, pageable);
            return BASKET_MAPPER.toDto(allByUserId);
    }
}
