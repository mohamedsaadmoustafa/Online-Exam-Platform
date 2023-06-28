package com.example.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exam_summary")
public class ExamSummary {
    @Id
    private String id;
    @Field(name = "user_id")
    private String userId;
    @Field(name = "exam_instance_id")
    private Long examInstanceId;
    private float score;
    private String status;

    public enum statusType {
        PASSED,
        FAILED,
        ABSENT
    }
}
