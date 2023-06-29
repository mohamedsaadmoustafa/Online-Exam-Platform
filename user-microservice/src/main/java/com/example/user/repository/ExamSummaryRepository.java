package com.example.user.repository;
import com.example.shared.model.ExamSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSummaryRepository extends PagingAndSortingRepository<ExamSummary, Long> {
    Page<ExamSummary> findByUserId(String userId, Pageable pageable);
}