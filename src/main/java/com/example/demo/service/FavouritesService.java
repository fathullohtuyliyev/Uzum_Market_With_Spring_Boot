package com.example.demo.service;


import com.example.demo.dto.delivery_dto.DeliveryCreateDto;
import com.example.demo.dto.delivery_dto.DeliveryGetDto;
import com.example.demo.dto.delivery_dto.DeliveryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public interface FavouritesService {
    Map<UUID, Map<UUID,UUID>> save(Map<UUID,UUID> dto);
    Map<UUID, Map<UUID,UUID>> update(Map<UUID, Map<UUID,UUID>> dto);
    void delete(UUID id,UUID userId);
    Map<UUID, Map<UUID,UUID>> get(UUID id);
    Page<Map<UUID, Map<UUID,UUID>>> users(UUID userId);
}
