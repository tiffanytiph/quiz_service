package com.tiffanytiph.quiz_service.dto.questions;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsAnswers {

    private UUID questionId;

    private String answer;

}
