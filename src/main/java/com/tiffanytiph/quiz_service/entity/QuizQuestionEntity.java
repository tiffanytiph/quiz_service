package com.tiffanytiph.quiz_service.entity;
import java.util.UUID;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "quiz_questions")
@SuperBuilder
public class QuizQuestionEntity {

    @Id
    @Column(unique = true, name = "id")
    @Builder.Default
    private UUID id = Generators.timeBasedEpochGenerator().generate();

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;
 
    private UUID questionId;
}
