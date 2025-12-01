package com.tiffanytiph.quiz_service.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tiffanytiph.quiz_service.dto.questions.QuestionsResponse;
import com.tiffanytiph.quiz_service.dto.quiz.QuizList;
import com.tiffanytiph.quiz_service.dto.quiz.QuizRequest;
import com.tiffanytiph.quiz_service.dto.quiz.QuizResponse;
import com.tiffanytiph.quiz_service.entity.QuizEntity;
import com.tiffanytiph.quiz_service.entity.QuizQuestionEntity;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    default QuizEntity populateQuizEntity(QuizRequest quizRequest) {
        return QuizEntity.builder()
                .title(quizRequest.getTitle())
                .category(quizRequest.getCategory())
                .build();
    }

    default QuizQuestionEntity populateQuizQuestionsEntity(QuizEntity quizEntity, UUID questionIds) {
        return QuizQuestionEntity.builder()
                .quiz(quizEntity)
                .questionId(questionIds)
                .build();
    }

    default QuizResponse toQuizResponse(QuizEntity quizEntity, List<QuestionsResponse> questions) {
        return QuizResponse.builder()
                .quizId(quizEntity.getId())
                .category(quizEntity.getCategory())
                .title(quizEntity.getTitle())
                .questions(questions)
                .build();
    }

    @Mapping(target = "quizId", source = "id")
    QuizList toQuizList(QuizEntity quizEntity);
    
}
