package com.example.demo.controller;

import com.example.demo.nosql.Comment;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<Comment> save(@RequestBody @Valid Comment comment){
        return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
    }
    @GetMapping("/get")
    public ResponseEntity<Comment> get(@RequestParam String id){
        return ResponseEntity.ok(commentService.get(UUID.fromString(id)));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam String id){
        commentService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get-by-good")
    public ResponseEntity<Page<Comment>> getByGood(@RequestParam Map<String, String> param){
        try {
            UUID goodId = UUID.fromString(param.get("goodId"));
            int page = Integer.parseInt(param.get("page"));
            int size = Integer.parseInt(param.get("size"));
            Page<Comment> comments = commentService.comments(goodId, PageRequest.of(page, size));
            return ResponseEntity.ok(comments);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
