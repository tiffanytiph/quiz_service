package com.tiffanytiph.quiz_service.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiffanytiph.quiz_service.dto.BaseResponse;
import com.tiffanytiph.quiz_service.dto.quiz.QuizRequest;
import com.tiffanytiph.quiz_service.dto.quiz.QuizScore;
import com.tiffanytiph.quiz_service.enums.Categories;
import com.tiffanytiph.quiz_service.enums.response.ResponseCode;
import com.tiffanytiph.quiz_service.service.QuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("quiz")
@RequiredArgsConstructor
public class QuizController extends BaseController {

    private final QuizService quizService;

    @GetMapping("list")
    public ResponseEntity<BaseResponse> getAllQuiz(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "category", required = false, defaultValue = "") Categories category
    ){
        return data(quizService.getAllQuiz(page, size, sortBy, sortDirection, category));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createQuiz(@RequestBody QuizRequest request) throws IOException {
        quizService.createQuiz(request);
        return success(ResponseCode.QUIZ_CREATED);
    }

    @PostMapping("/get_score")
    public ResponseEntity<BaseResponse> getScore(@RequestBody QuizScore request) throws IOException {
        return data(quizService.getScore(request));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<BaseResponse> getQuiz(@PathVariable UUID quizId) throws IOException {
        return data(quizService.getQuiz(quizId));
    }

}
