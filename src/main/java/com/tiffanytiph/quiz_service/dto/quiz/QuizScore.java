package com.tiffanytiph.quiz_service.dto.quiz;

import java.util.List;
import java.util.UUID;

import com.tiffanytiph.quiz_service.dto.questions.QuestionsAnswers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizScore {

    private UUID quizId;

    private List<QuestionsAnswers> answers;

}
