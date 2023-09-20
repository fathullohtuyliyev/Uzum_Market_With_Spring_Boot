package com.example.demo.repository;

import com.example.demo.nosql.Images;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends MongoRepository<Images, UUID> {
}
