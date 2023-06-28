package com.example.exam.repository;
import com.example.exam.model.ExamInstance;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ExamInstanceRepository extends JpaRepository<ExamInstance, Long> {
    List<ExamInstance> findByGeneratedLinkScheduledTimeToBetween(LocalDateTime now, LocalDateTime scheduledTimeTo);
}