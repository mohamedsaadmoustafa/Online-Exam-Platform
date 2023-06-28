package com.example.exam.DTO;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class ExamDTO {
    private Set<Question> questions = new HashSet<>();

    @Data
    static
    class Question {
        private String id;
        private String name;
        private Answer[] answers;
    }
    @Data
    public static
    class Answer {
        private String id;
        private String answerName;
        private String answerDescription;

    }
}
