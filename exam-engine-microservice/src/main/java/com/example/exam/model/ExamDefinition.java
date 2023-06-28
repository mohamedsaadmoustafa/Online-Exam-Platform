package com.example.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "exam_definition")
public class ExamDefinition {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "passing_score", nullable = false)
    private Float passingScore = 0.5F;

    @Column(name = "created_by")
    private String createdBy;

    @ElementCollection
    //@ElementCollection(fetch = FetchType.LAZY)  // lazy loading: defer loading the collection until it is actually needed
    private Set<String> questions = new HashSet<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}