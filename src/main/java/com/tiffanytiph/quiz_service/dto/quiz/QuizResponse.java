package com.tiffanytiph.quiz_service.dto.quiz;

import java.util.List;
import com.tiffanytiph.quiz_service.dto.questions.QuestionsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizResponse extends QuizList {

    private List<QuestionsResponse> questions;

}
