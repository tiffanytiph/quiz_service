package com.tiffanytiph.quiz_service.dto.questions;

import java.util.UUID;

import com.tiffanytiph.quiz_service.enums.Categories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsResponse {

    private UUID id;

    private Categories category;

    private String question;

    private String optOne;

    private String optTwo;

    private String optThree;

    private String optFour;

}
