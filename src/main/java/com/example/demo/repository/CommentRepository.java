package com.example.demo.repository;

import com.example.demo.entity.Good;
import java.util.List;
import com.example.demo.nosql.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CommentRepository extends MongoRepository<Comments, UUID> {
    @Query
    Page<Comments> findByGoodId(UUID goodId, Pageable pageable);

    @Query
    int findAllSizeByGoodId(UUID id);

    @Query(value = "{goodId: ?1}")
    Page<Comments> findAllByCommentId(UUID goodId, Pageable pageable);

    @Query(value = "{goodId: ?1}")
    List<Comments> findAllByGoodId(UUID goodId);
}
