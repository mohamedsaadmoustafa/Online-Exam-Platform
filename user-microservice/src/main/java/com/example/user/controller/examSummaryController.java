package com.example.user.controller;

import com.example.shared.model.ExamSummary;
import com.example.user.service.ExamSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@CrossOrigin
public class examSummaryController {
    private final ExamSummaryService examSummaryService;

    public examSummaryController(
            ExamSummaryService examSummaryService) {
        this.examSummaryService = examSummaryService;
    }

    @GetMapping("/user/{userId}/exam-summary")
    public List<ExamSummary> getExamSummariesForUser(
            @PathVariable String userId,
            @RequestParam int pageNo) {
        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number should be greater than 0.");
        }
        return this.examSummaryService.getAllExamSummariesForUser(userId, pageNo);
    }

    @GetMapping("/user/{userId}/exam-summary/count")
    public long  getExamSummaryCountByUserId(@PathVariable String userId){
        return this.examSummaryService.getExamSummaryCountByUserId(userId);
    }
}