package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.promo_code_dto.PromoCodeCreateDto;
import com.example.demo.dto.promo_code_dto.PromoCodeGetDto;
import com.example.demo.dto.promo_code_dto.PromoCodeUpdateDto;
import com.example.demo.entity.Good;
import com.example.demo.entity.PromoCode;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.PromoCodeRepository;
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

import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;
import static com.example.demo.mapper.PromoCodeMapper.PROMO_CODE_MAPPER;
import static com.example.demo.mapper.UserMapper.USER_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Override
    public PromoCodeGetDto save(PromoCodeCreateDto dto) {
        try {
            PromoCode promoCode = PROMO_CODE_MAPPER.toEntity(dto);
            method1(dto,promoCode);
            PromoCode saved = promoCodeRepository.save(promoCode);
            PromoCodeGetDto dto1 = PROMO_CODE_MAPPER.toDto(saved);
            method2(dto1,saved);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void method2(PromoCodeGetDto dto, PromoCode promoCode) {
        List<GoodGetDto> list = promoCode.getGoods()
                .stream()
                .map(GOOD_MAPPER::toDto)
                .toList();
        dto.setGoods(list);

        List<AuthUserGetDto> users = null;
        try {
            users = promoCode.getAuthUsers()
                    .stream()
                    .map(USER_MAPPER::toDto)
                    .toList();
        }catch (Exception ignore){}
        dto.setUsers(users);
    }

    private static void method1(PromoCodeCreateDto dto, PromoCode promoCode) {
        List<Good> list = dto.getGoods()
                .stream()
                .map(GOOD_MAPPER::toEntity)
                .toList();
        promoCode.setGoods(list);
    }
    private static void method1(PromoCodeUpdateDto dto, PromoCode promoCode) {
        List<Good> list = dto.getGoods()
                .stream()
                .map(GOOD_MAPPER::toEntity)
                .toList();
        promoCode.setGoods(list);
    }

    @Override
    public PromoCodeGetDto update(PromoCodeUpdateDto dto) {
        try {
            PromoCode promoCode = PROMO_CODE_MAPPER.toEntity(dto);
            method1(dto,promoCode);
            promoCodeRepository.updatePromoCode(promoCode.getName(),
                    promoCode.isActive(),promoCode.getGoods(),promoCode.getId());
            PromoCode found = promoCodeRepository.findById(dto.id).orElseThrow(NotFoundException::new);
            PromoCodeGetDto dto1 = PROMO_CODE_MAPPER.toDto(found);
            method2(dto1,found);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(UUID id) {
         try {
             promoCodeRepository.deleteWithId(id);
         }catch (Exception e){
             e.printStackTrace();
             Arrays.stream(e.getStackTrace())
                     .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
             throw new RuntimeException();
         }
    }

    @Override
    public PromoCodeGetDto get(UUID id) {
        try {
            PromoCode promoCode = promoCodeRepository.findById(id).orElseThrow(NotFoundException::new);
            PromoCodeGetDto dto = PROMO_CODE_MAPPER.toDto(promoCode);
            method2(dto,promoCode);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<PromoCodeGetDto> promoCodes(UUID goodId, Pageable pageable) {
        try {
            Page<PromoCode> result = promoCodeRepository.findAllByGoodId(goodId,pageable);
            int size = promoCodeRepository.findAllByGoodIdSize(goodId);
            if (size<pageable.getPageSize()) {
                List<PromoCode> allByGoodId = promoCodeRepository.findAllByGoodId(goodId);
                result = new PageImpl<>(allByGoodId, PageRequest.of(0,size),size);
            }
            Page<PromoCodeGetDto> dto = PROMO_CODE_MAPPER.toDto(result);
            methodList(result, dto);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
    private static void methodList(Page<PromoCode> result, Page<PromoCodeGetDto> dto){
        List<PromoCodeGetDto> contentDto = dto.getContent();
        List<PromoCode> content = result.getContent();
        if (contentDto.isEmpty()) {
            return;
        }
        for (int i = 0; i < content.size(); i++) {
            method2(contentDto.get(i),content.get(i));
        }
    }
    @Override
    public PromoCodeGetDto getByName(String name) {
        try {
            PromoCode promoCode = promoCodeRepository.findByName(name).orElseThrow(NotFoundException::new);
            PromoCodeGetDto dto = PROMO_CODE_MAPPER.toDto(promoCode);
            method2(dto,promoCode);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
