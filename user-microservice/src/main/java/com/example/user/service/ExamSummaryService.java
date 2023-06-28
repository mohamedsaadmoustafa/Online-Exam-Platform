package com.example.user.service;

import com.example.shared.model.ExamSummary;
import com.example.user.repository.ExamSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ExamSummaryService {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ExamSummaryService.class);
    private final ExamSummaryRepository examSummaryRepository;
    private final int pageSize;
    private final MongoTemplate mongoTemplate;

    public ExamSummaryService(ExamSummaryRepository examSummaryRepository,
                              MongoTemplate mongoTemplate,
                              @Value("${page.default-page-size}") int pageSize) {
        this.examSummaryRepository = examSummaryRepository;
        this.mongoTemplate = mongoTemplate;
        this.pageSize = pageSize;
    }

    public void saveExamSummary(ExamSummary examSummary){
        this.examSummaryRepository.save(examSummary);
        LOG.info("Exam summary saved successfully.");
    }

    public List<ExamSummary> getAllExamSummariesForUser(String userId, int pageNumber){
        if (userId == null || userId.isEmpty()) {
            //throw new IllegalArgumentException("User ID cannot be null or empty.");
            LOG.warn("User ID cannot be null or empty.");
        }
        if (pageNumber < 1) {
            //throw new IllegalArgumentException("Page number should be greater than 0.");
            LOG.warn("Page number should be greater than 0.");
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return examSummaryRepository.findByUserId(userId, pageable).getContent();
    }

    public long  getExamSummaryCountByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.count(query, ExamSummary.class);
    }
}
