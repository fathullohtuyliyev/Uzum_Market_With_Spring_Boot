package com.example.demo.service;

import com.example.demo.dto.basket_dto.BasketCreateDto;
import com.example.demo.dto.basket_dto.BasketGetDto;
import com.example.demo.entity.Basket;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

import static com.example.demo.mapper.BasketMapper.BASKET_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final AuthUserRepository authUserRepository;
    private final BasketRepository basketRepository;

    @Override
    public Page<BasketGetDto> save(BasketCreateDto dto) {
        try {
            checkMethod(dto.userId,authUserRepository);
            basketRepository.saveNewBasket(dto.userId,dto.goodId);
            Page<Basket> baskets = basketRepository.findAllByUserId(dto.userId, PageRequest.of(0,20));
            return BASKET_MAPPER.toDto(baskets);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<BasketGetDto> delete(UUID goodId,UUID userId) {
        try {
            checkMethod(userId,authUserRepository);
            basketRepository.deleteByGoodIdAndUserId(goodId,userId);
            Page<Basket> allByUserId = basketRepository.findAllByUserId(userId, PageRequest.of(0, 20));
            return BASKET_MAPPER.toDto(allByUserId);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void checkMethod(UUID userId,AuthUserRepository authUserRepository){
        if(!authUserRepository.isActive(userId)){
            throw new ForbiddenAccessException();
        }
    }
    @Override
    public Page<BasketGetDto> get(UUID userId,Pageable pageable) {
        try {
            checkMethod(userId,authUserRepository);
            Page<Basket> allByUserId = basketRepository.findAllByUserId(userId, pageable);
            int allSize = basketRepository.findAllSize(userId);
            if (allSize <allByUserId.getContent().size()) {
                allByUserId = new PageImpl<>(basketRepository.findAllByUserId(userId),
                        PageRequest.of(0,allSize),allSize);
            }
            return BASKET_MAPPER.toDto(allByUserId);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
