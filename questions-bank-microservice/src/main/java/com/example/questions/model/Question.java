package com.example.questions.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection="question")
public class Question {
    @Id
    private String id;
    private String name;
    @Field(name = "level_id")
    private Integer levelId;
    private String category;
    @Field(name = "sub_category")
    private String subCategory;
    private float mark;
    @Field(name = "expected_time")
    private int expectedTime;
    @Field(name = "created_by")
    private String createdBy;
    @Field(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Field(name = "correct_answer_ids")
    private Set<String> correctAnswerIds = new HashSet<>();
    private Set<Answer> answers = new HashSet<>();

    @Data
    public static class Answer {
        private String id;
        private String answerName;
        private String answerDescription;
    }
}