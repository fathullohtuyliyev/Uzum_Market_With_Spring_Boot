package com.example.demo.service;

import com.example.demo.nosql.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface GoodCommentService {
    Page<Comment> getComments(UUID goodId, Pageable pageable);
    Comment addComment(UUID goodId, Short rating, UUID userId, List<String> images, String message);
    Comment responseToComment(UUID id, String response);
    void spam(UUID id, UUID userId);
}
