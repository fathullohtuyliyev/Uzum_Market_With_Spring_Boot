package com.example.demo.repository;

import java.util.List;
import com.example.demo.nosql.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CommentRepository extends MongoRepository<Comment, UUID> {
    @Query
    Page<Comment> findByGoodId(UUID goodId, Pageable pageable);

    @Query
    int findAllSizeByGoodId(UUID id);

    @Query(value = "{goodId: ?1}")
    Page<Comment> findAllByCommentId(UUID goodId, Pageable pageable);

    @Query(value = "{goodId: ?1}")
    List<Comment> findAllByGoodId(UUID goodId);
}
