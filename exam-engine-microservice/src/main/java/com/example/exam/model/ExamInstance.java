package com.example.exam.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exam_instance")
public class ExamInstance {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "exam_definition_id")  // link by id  ,
    private Long examDefinitionId;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime  endTime;
    @Column(name = "duration_in_minutes")
    private int durationInMinutes = 60;
    @Column(name = "completion_time")
    private LocalDateTime  completionTime;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "generated_link_id", referencedColumnName = "id")
    private GeneratedLink generatedLink;
    @Column(name = "assigned_by")
    private String assignedBy;
    @Column(name = "taken_by")
    private String takenBy;
    private String status;                              // assigned, absent, taken(passed/failed)
    @OneToMany(cascade = CascadeType.ALL)              // , fetch = FetchType.LAZY
    @JoinColumn(name = "exam_instance_id")
    private Set<Question> questions = new HashSet<>();

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        this.completionTime = this.startTime.plusMinutes(this.durationInMinutes);
    }

    public void addQuestions(Set<String> questionIds) {
        for (String questionId : questionIds) {
            this.questions.add(new Question(questionId));
        }
    }

    public Set<Question> getQuestions(){
        return this.questions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "generated_link")
    public static class GeneratedLink {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name="id")
        private Long id;
        @Column(name = "scheduled_time_from")
        private LocalDateTime  scheduledTimeFrom;
        @Column(name = "scheduled_time_to")
        private LocalDateTime  scheduledTimeTo;
//        private String token;
        private String url;
//
//        public String setUrl(String path) {
//            this.token = UUID.randomUUID().toString();
//            this.url = path + this.token;
//            return this.url;
//        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "question")
    public static class Question {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name="id")
        private Long     id;
        @Column(name = "question_id", nullable = false)
        private String questionId;
        @Column(name = "selected_answer_id")

        //@ElementCollection(fetch = FetchType.LAZY)
        @ElementCollection()
        private Set<String> selectedAnswerIds = new HashSet<>();

        @Column(name = "display_time")
        private LocalDateTime  displayTime;
        @Column(name = "answer_time")
        private LocalDateTime  answerTime;

        public Question(String questionId) {
            this.questionId = questionId;
        }
    }
}