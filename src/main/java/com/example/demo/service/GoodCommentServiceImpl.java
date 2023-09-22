package com.example.demo.service;

import com.example.demo.entity.Good;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.nosql.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodCommentServiceImpl implements GoodCommentService {
    private final GoodRepository goodRepository;
    private final CommentRepository commentRepository;
    @Override
    public Page<Comment> getComments(UUID goodId, Pageable pageable) {
        try {
            return commentRepository.findByGoodId(goodId, pageable);
        }catch (Exception e){
            log.error("Error processing spam method", e);
            throw new RuntimeException("Error processing spam method", e);
        }
    }

    @Override
    public Comment addComment(UUID goodId, Short rating, UUID userId, List<String> images ,  String message) {
        try {
            Comment comment = Comment.builder()
                    .message(message)
                    .goodId(goodId)
                    .rating(rating)
                    .userId(userId)
                    .images(images)
                    .commentDate(LocalDateTime.now())
                    .build();
            return commentRepository.save(comment);
        }catch (Exception e){
            log.error("Error processing spam method", e);
            throw new RuntimeException("Error processing spam method", e);
        }
    }

    @Override
    public Comment responseToComment(UUID id, String response) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            comment.setResponse(response);
            return commentRepository.save(comment);
        }catch (Exception e){
            log.error("Error processing spam method", e);
            throw new RuntimeException("Error processing spam method", e);
        }
    }

    @Override
    public synchronized void spam(UUID id, UUID userId) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            Set<UUID> spammedUsers = comment.getSpammedUsers();
            if (!spammedUsers.add(userId)) {
                throw new ForbiddenAccessException();
            }
            comment.setSpammedUsers(spammedUsers);
            commentRepository.save(comment);
            int size = commentRepository.findAllByGoodId(comment.getGoodId()).size();

            int spamSize = comment.getSpammedUsers().size();
            if (size/2 >= spamSize) {
                commentRepository.delete(comment);
            }
        } catch (Exception e){
            log.error("Error processing spam method", e);
            throw new RuntimeException("Error processing spam method", e);
        }
    }
}
