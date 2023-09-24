package com.example.demo.service;

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

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        try {
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
                comments = new PageImpl<>(comments.getContent(), PageRequest.of(0,size),size);
            }
            return comments;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
