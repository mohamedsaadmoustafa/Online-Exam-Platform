package com.example.notifications.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
public class ExamInstanceDTO {
    private Long id;
    private Long examDefinitionId;
    private LocalDateTime startTime;
    private LocalDateTime  endTime;
    private int durationInMinutes;
    private LocalDateTime  completionTime;
    private GeneratedLink generatedLink;
    private String assignedBy;
    private String takenBy;
    private String status;
    private Set<Question> questions = new HashSet<>();

    @Data
    @Getter
    @Setter
    public static class GeneratedLink {
        private Long id;
        private LocalDateTime  scheduledTimeFrom;
        private LocalDateTime  scheduledTimeTo;
        private String token;
        private String url;
    }

    @Data
    @Getter
    @Setter
    public static class Question {
        private Long     id;
        private String questionId;
        private Set<String> selectedAnswerIds = new HashSet<>();
        private LocalDateTime  displayTime;
        private LocalDateTime  answerTime;
    }
}