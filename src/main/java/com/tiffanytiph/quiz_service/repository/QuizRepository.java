package com.tiffanytiph.quiz_service.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tiffanytiph.quiz_service.entity.QuizEntity;
import com.tiffanytiph.quiz_service.enums.Categories;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, UUID> {

    @Query("""
        SELECT q 
        FROM QuizEntity q
        WHERE (:category IS NULL OR q.category = :category) 
    """)
    Page<QuizEntity> searchQuiz(
        Pageable pageable, 
        @Param("category") Categories category);

}
