package com.tiffanytiph.quiz_service.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.tiffanytiph.quiz_service.enums.Categories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz")
@SuperBuilder
public class QuizEntity {

    @Id
    @Column(unique = true, name = "id")
    @Builder.Default
    private UUID id = Generators.timeBasedEpochGenerator().generate();

    @Enumerated(EnumType.STRING)
    private Categories category;    

    private String title;
    
    @OneToMany(mappedBy = "quiz")
    private List<QuizQuestionEntity> quizQuestions;
}
