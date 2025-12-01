package com.tiffanytiph.quiz_service.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiffanytiph.quiz_service.dto.BaseResponse;
import com.tiffanytiph.quiz_service.dto.questions.QuestionsResponse;
import com.tiffanytiph.quiz_service.dto.questions.ScoreResult;
import com.tiffanytiph.quiz_service.dto.quiz.QuizRequest;
import com.tiffanytiph.quiz_service.dto.quiz.QuizResponse;
import com.tiffanytiph.quiz_service.dto.quiz.QuizScore;
import com.tiffanytiph.quiz_service.entity.QuizEntity;
import com.tiffanytiph.quiz_service.entity.QuizQuestionEntity;
import com.tiffanytiph.quiz_service.enums.Categories;
import com.tiffanytiph.quiz_service.enums.response.ResponseCode;
import com.tiffanytiph.quiz_service.feign.QuestionFeign;
import com.tiffanytiph.quiz_service.mapper.QuizMapper;
import com.tiffanytiph.quiz_service.repository.QuizQuestionRepository;
import com.tiffanytiph.quiz_service.repository.QuizRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService extends AppService {

    private final QuizRepository quizRepository;

    private final QuizQuestionRepository quizQuestionRepository;

    private final QuestionFeign questionFeign;

    private final QuizMapper quizMapper;

    private final ObjectMapper objectMapper;

    public QuizEntity getQuestionById(UUID questionId){
        return quizRepository.findById(questionId)
            .orElseThrow(() -> new EntityNotFoundException(
                getMessage(ResponseCode.QUIZ_NOT_FOUND, questionId.toString())));
    }

    @Transactional
    public void createQuiz(QuizRequest request) throws IOException {
        // call question service to get questions
        BaseResponse response = questionFeign.getQuestionsForQuiz(
            request.getCategory(), request.getTotalQuestions())
            .getBody();
        
        // create quiz entity
        QuizEntity quizEntity = quizMapper.populateQuizEntity(request);
        QuizEntity newQuizEntity = quizRepository.save(quizEntity);

        // create quiz question entities
        List<QuizQuestionEntity> quizQuestionEntities = new ArrayList<>();
        Object data = response.getData();
        List<UUID> result = objectMapper.convertValue(
            data,
            new TypeReference<List<UUID>>() {}
        );
        for (UUID id : result) {
            quizQuestionEntities.add(
                quizMapper.populateQuizQuestionsEntity(
                    newQuizEntity, id )
            );
        }
        quizQuestionRepository.saveAll(quizQuestionEntities);
    }

	public Double getScore(QuizScore request) {
		// call question service to get score
        BaseResponse response = questionFeign.calculateScore(
            request.getAnswers()
        ).getBody();
 
        // process the response
        Object data = response.getData();
        ScoreResult result = objectMapper.convertValue(
            data, ScoreResult.class);

        return result.getScore();
	}

	public QuizResponse getQuiz(UUID quizId) {
		QuizEntity quizEntity = this.getQuestionById(quizId);
        List<UUID> questionIds = quizEntity.getQuizQuestions()
            .stream()
            .map(QuizQuestionEntity::getQuestionId)
            .toList();
        System.out.println(questionIds);

        // call question service to get questions
        BaseResponse response = questionFeign.getQuestionsByIds(
            questionIds
        ).getBody();
        Object data = response.getData();
        List<QuestionsResponse> questions = objectMapper.convertValue(
            data,
            new TypeReference<List<QuestionsResponse>>() {}
        );

        return quizMapper.toQuizResponse(quizEntity, questions);
	}

    public Object getAllQuiz(int page, int size, String sortBy, 
        String sortDirection, Categories category) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);
        Page<QuizEntity> quizes = quizRepository.searchQuiz(pageable, category);

        return quizes.map(quiz -> quizMapper.toQuizList(quiz));
    }

}
