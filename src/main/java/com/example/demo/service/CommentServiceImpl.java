package com.example.demo.service;

import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.nosql.Comment;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
            comment.setSpammedUsers(null);
            comment.setResponse(null);
            comment.setResponseDate(null);
            comment.setCommentDate(LocalDateTime.now());
            return commentRepository.save(comment);
    }

    @Override
    public Comment get(UUID id) {
            return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(UUID id) {
            commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> comments(UUID goodId, Pageable pageable) {
            return commentRepository.findByGoodId(goodId, pageable);
    }
    @Override
    public Comment responseToComment(UUID id, String response) {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            comment.setResponseDate(LocalDateTime.now());
            comment.setResponse(response);
            return commentRepository.save(comment);
    }

    @Override
    public synchronized void spam(UUID id, UUID userId) {
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
    }
}
