package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {
    @Override
    public Map<UUID, Map<UUID, UUID>> save(Map<UUID, UUID> dto) {
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, UUID>> update(Map<UUID, Map<UUID, UUID>> dto) {
        return null;
    }

    @Override
    public void delete(UUID id,UUID userId) {

    }

    @Override
    public Map<UUID, Map<UUID, UUID>> get(UUID id) {
        return null;
    }

    @Override
    public Page<Map<UUID, Map<UUID, UUID>>> users(UUID userId) {
        return null;
    }
}
