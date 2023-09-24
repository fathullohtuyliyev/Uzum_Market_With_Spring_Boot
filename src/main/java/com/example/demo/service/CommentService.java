package com.example.demo.service;

import com.example.demo.nosql.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CommentService {
    Comment save(Comment comment);
    Comment get(UUID id);
    void delete(UUID id);
    Page<Comment> comments(UUID goodId, Pageable pageable);
}
