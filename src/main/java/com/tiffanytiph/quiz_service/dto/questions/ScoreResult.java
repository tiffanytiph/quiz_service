package com.tiffanytiph.quiz_service.dto.questions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreResult {

    private int totalQuestions;

    private long correctAnswers;

    private double score;

}
