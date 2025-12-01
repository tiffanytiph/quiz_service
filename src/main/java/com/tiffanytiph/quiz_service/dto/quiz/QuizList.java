package com.tiffanytiph.quiz_service.dto.quiz;

import java.util.UUID;

import com.tiffanytiph.quiz_service.enums.Categories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizList {

    private UUID quizId;

    private Categories category;

    private String title;

}
