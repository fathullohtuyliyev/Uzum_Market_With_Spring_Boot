package com.example.demo.controller;

import com.example.demo.nosql.Comment;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api.comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<Comment> save(@RequestBody @Valid Comment comment){
        return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
    }
    @GetMapping("/get")
    @Cacheable(key = "#id",value = "comments")
    public ResponseEntity<Comment> get(@RequestParam String id){
        return ResponseEntity.ok(commentService.get(UUID.fromString(id)));
    }
    @DeleteMapping("/delete")
    @CacheEvict(key = "#id",value = "comments")
    public ResponseEntity<Void> delete(@RequestParam String id){
        commentService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get-by-good")
    @Cacheable(key = "#param",value = "comments")
    public ResponseEntity<Page<Comment>> getByGood(@RequestParam Map<String, String> param){
        try {
            UUID goodId = UUID.fromString(param.get("goodId"));
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            Page<Comment> comments = commentService.comments(goodId, PageRequest.of(page, size));
            return ResponseEntity.ok(comments);
        }catch (IllegalArgumentException e){
            log.error("Error while getting comments",e);
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAuthority('SELLER')")
    @PostMapping("/response")
    public ResponseEntity<Comment> response(@RequestParam String id,
                                            @RequestParam String message){
        try {
            Comment comment = commentService
                    .responseToComment(UUID.fromString(id), message);
            return ResponseEntity.ok(comment);
        }catch (IllegalArgumentException e){
            log.error("Error while getting comments",e);
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/spam")
    public ResponseEntity<Void> spam(@RequestParam String id,
                                     @RequestParam String userId){
        try {
            commentService.spam(UUID.fromString(id),UUID.fromString(userId));
            return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e){
            log.error("Error while spamming",e);
            return ResponseEntity.badRequest().build();
        }
    }
}
