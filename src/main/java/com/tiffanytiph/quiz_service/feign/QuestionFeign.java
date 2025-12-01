package com.tiffanytiph.quiz_service.feign;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tiffanytiph.quiz_service.dto.BaseResponse;
import com.tiffanytiph.quiz_service.dto.questions.QuestionsAnswers;
import com.tiffanytiph.quiz_service.enums.Categories;

@FeignClient("QUESTION-SERVICE")
public interface QuestionFeign {

    @GetMapping("questions/random_question")
    public ResponseEntity<BaseResponse> getQuestionsForQuiz(
        @RequestParam Categories category, 
        @RequestParam Integer totalQuestion
    );

    @PostMapping("questions/calculate_score")
    public ResponseEntity<BaseResponse> calculateScore(
        @RequestBody List<QuestionsAnswers> answers
    );
    
    @PostMapping("questions/ids")
    public ResponseEntity<BaseResponse> getQuestionsByIds(
        @RequestBody List<UUID> ids
    );
}
