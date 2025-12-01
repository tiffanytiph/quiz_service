package com.tiffanytiph.quiz_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiffanytiph.quiz_service.entity.QuizQuestionEntity;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, UUID> {

}
