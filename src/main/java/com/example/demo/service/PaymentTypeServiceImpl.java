package com.example.demo.service;

import com.example.demo.dto.payment_dto.PaymentGetDto;
import com.example.demo.dto.payment_dto.PaymentUpdateDto;
import com.example.demo.entity.PaymentType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.mapper.PaymentMapper.PAYMENT_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public PaymentGetDto save(String name) {
            PaymentType paymentType = PaymentType.builder()
                    .name(name)
                    .build();
            PaymentType saved = paymentTypeRepository.save(paymentType);
            PaymentGetDto dto = PAYMENT_MAPPER.toDto(saved);
            log.info("{} saved",dto);
            return dto;
    }

    @Override
    public PaymentGetDto update(PaymentUpdateDto dto) {
            PaymentType paymentType = PAYMENT_MAPPER.toEntity(dto);
            paymentTypeRepository.updateActive(paymentType.isActive(),paymentType.getName() , paymentType.getId());
            PaymentType found = paymentTypeRepository.findById(paymentType.getId()).orElseThrow(NotFoundException::new);
            PaymentGetDto dto1 = PAYMENT_MAPPER.toDto(found);
            log.info("{} updated",dto1);
            return dto1;
    }

    @Override
    public void delete(Integer id) {
            paymentTypeRepository.deleteWithId(id);
    }

    @Override
    public PaymentGetDto get(Integer id) {
            PaymentType paymentType = paymentTypeRepository.findById(id).orElseThrow(NotFoundException::new);
            PaymentGetDto dto = PAYMENT_MAPPER.toDto(paymentType);
            log.info("{} gave",dto);
            return dto;
    }

    @Override
    public Page<PaymentGetDto> paymentTypes(Pageable pageable) {
            Page<PaymentType> all = paymentTypeRepository.findAll(pageable);
            List<Integer> list = all.stream()
                    .map(PaymentType::getId)
                    .toList();
            log.info("{} gave",list);
            return PAYMENT_MAPPER.toDto(all);
    }
}
