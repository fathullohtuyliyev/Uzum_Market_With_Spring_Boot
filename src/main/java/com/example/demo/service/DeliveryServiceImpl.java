package com.example.demo.service;

import com.example.demo.dto.delivery_dto.DeliveryAddressDto;
import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import com.example.demo.entity.DeliveryPoint;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.DeliveryPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mapper.DeliveryMapper.DELIVERY_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryPointRepository deliveryPointRepository;

    @Override
    public DeliveryGetDto save(DeliveryCreateDto dto) {
            DeliveryPoint deliveryPoint = DELIVERY_MAPPER.toEntity(dto);
            method1(deliveryPoint,dto);
            DeliveryPoint saved = deliveryPointRepository.save(deliveryPoint);
            DeliveryGetDto dto1 = DELIVERY_MAPPER.toDto(saved);
            method2(dto1,saved);
            return dto1;
    }

    private static void method1(DeliveryPoint deliveryPoint, DeliveryCreateDto dto){
        Double lat = dto.getDeliveryAddress().lat;
        Double lng = dto.getDeliveryAddress().lng;
        deliveryPoint.setDeliveryAddress(new double[]{lat, lng});
    }
    private static void method1(DeliveryPoint deliveryPoint, DeliveryUpdateDto dto){
        Double lat = dto.getDeliveryAddress().lat;
        Double lng = dto.getDeliveryAddress().lng;
        deliveryPoint.setDeliveryAddress(new double[]{lat, lng});
    }
    private static void method2(DeliveryGetDto dto,DeliveryPoint deliveryPoint) {
        double[] deliveryAddress = deliveryPoint.getDeliveryAddress();
        dto.setDeliveryAddress(DeliveryAddressDto.builder()
                .lat(deliveryAddress[0])
                .lng(deliveryAddress[1]).build());
    }

    @Override
    public DeliveryGetDto update(DeliveryUpdateDto dto) {
            DeliveryPoint deliveryPoint = DELIVERY_MAPPER.toEntity(dto);
            method1(deliveryPoint,dto);
            DeliveryPoint elseThrow = deliveryPointRepository.findById(deliveryPoint.getId())
                    .orElseThrow(NotFoundException::new);
            deliveryPoint.setOrders(elseThrow.getOrders());
            DeliveryPoint saved = deliveryPointRepository.save(deliveryPoint);
            DeliveryGetDto dto1 = DELIVERY_MAPPER.toDto(saved);
            method2(dto1,saved);
            return dto1;
    }


    @Override
    public DeliveryGetDto get(Long id) {
            DeliveryPoint deliveryPoint = deliveryPointRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            DeliveryGetDto dto = DELIVERY_MAPPER.toDto(deliveryPoint);
            method2(dto,deliveryPoint);
            return dto;
    }

    @Override
    public Page<DeliveryGetDto> deliveryPoints(Pageable pageable) {
            Page<DeliveryPoint> all = deliveryPointRepository.findAll(pageable);
            return DELIVERY_MAPPER.toDto(all);
    }
}
