package com.tiffanytiph.quiz_service.dto.quiz;

import com.tiffanytiph.quiz_service.enums.Categories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizRequest {

    private String title;

    private Categories category;

    private Integer totalQuestions;

}
