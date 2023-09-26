package com.example.demo.service;

import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.nosql.Comment;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        try {
            comment.setSpammedUsers(null);
            comment.setResponse(null);
            comment.setResponseDate(null);
            comment.setCommentDate(LocalDateTime.now());
            return commentRepository.save(comment);
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Comment get(UUID id) {
        try {
            return commentRepository.findById(id).orElseThrow(NotFoundException::new);
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            commentRepository.deleteById(id);
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<Comment> comments(UUID goodId, Pageable pageable) {
        try {
            Integer size = commentRepository.findAllSizeByGoodId(goodId);
            Page<Comment> comments = commentRepository.findByGoodId(goodId, pageable);
            if (pageable.getPageSize()>size) {
//                comments = new PageImpl<>(comments.getContent(), PageRequest.of(0,size),size);
            }
            return comments;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
    @Override
    public Comment responseToComment(UUID id, String response) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            comment.setResponseDate(LocalDateTime.now());
            comment.setResponse(response);
            return commentRepository.save(comment);
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            log.error("Error processing response method", e);
            throw new RuntimeException("Error processing response method", e);
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
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            log.error("Error processing spam method", e);
            throw new RuntimeException("Error processing spam method", e);
        }
    }
}
