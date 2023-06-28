package com.example.exam.service;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamDefinition;
import com.example.exam.model.ExamInstance;
import com.example.exam.repository.ExamDefinitionRepository;
import com.example.exam.repository.ExamInstanceRepository;
import com.example.exam.util.HelperUtil;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamInstanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamInstanceService.class);

    @Value("${notifications.management.microservice.url}")
    String notificationServiceURL;

    private final ExamInstanceRepository examInstanceRepository;
    private final ExamDefinitionRepository examDefinitionRepository;
    private final HelperUtil helperUtil;

    public ExamInstanceService(
            ExamInstanceRepository examInstanceRepository,
            ExamDefinitionRepository examDefinitionRepository,
            HelperUtil helperUtil
    ) {
        this.examInstanceRepository = examInstanceRepository;
        this.examDefinitionRepository = examDefinitionRepository;
        this.helperUtil = helperUtil;
    }

    public Set<String> getQuestionIdsFromExamDefinition(Long examDefinitionId) throws ExamException {
        ExamDefinition examDefinition = examDefinitionRepository.findById(examDefinitionId).orElse(null);
        if (examDefinition == null) {
            throw new ExamException("Exam definition not found");
        }
        return examDefinition.getQuestions();
    }

    public ExamInstance createExamInstance(@NonNull ExamInstance examInstance) throws Exception {
        if (examInstance.getExamDefinitionId() == null) {
            throw new ExamException("Exam definition ID should be provided");
        }
        Set<String> questionIds = getQuestionIdsFromExamDefinition(examInstance.getExamDefinitionId());
        examInstance.addQuestions(questionIds);
        examInstance.setStatus("assigned");
        // remove this after solve scheduledTimeFrom, scheduledTimeTo null problem
        LocalDateTime startTime = examInstance.getStartTime(); // get the start time
        if (startTime == null) {
            startTime = LocalDateTime.now(); // set to current time if not provided
            examInstance.setStartTime(startTime);
        }
        int durationInMinutes = examInstance.getDurationInMinutes();
        LocalDateTime endTime = examInstance.getEndTime(); // get the end time
        if (endTime == null && durationInMinutes > 0) {
            endTime = startTime.plusMinutes(durationInMinutes); // calculate end time based on duration
            examInstance.setEndTime(endTime);
        }
        //examInstance.getGeneratedLink().setUrl("");

        // send notification to app-exam-engine
        String url = this.notificationServiceURL + "/receive-exam";
        helperUtil.postObjectToNotifications(examInstance, url);

        return examInstanceRepository.save(examInstance);

    }

    public Set<ExamInstance> getAllExamInstances() {
        List<ExamInstance> examInstances = examInstanceRepository.findAll();
        return new HashSet<>(examInstances);
    }

    public Set<ExamInstance> getCurrentAndUnfinishedExams(String studentId) throws ExamException {
        try {
            Set<ExamInstance> allExams = this.getAllExamInstancesForStudent(studentId);
            if (allExams.isEmpty()) {
                return allExams;
            }

            Set<ExamInstance> currentAndUnfinishedExams = new HashSet<>();
            for (ExamInstance examInstance : allExams) {
                LocalDateTime scheduledTimeFrom = examInstance.getGeneratedLink().getScheduledTimeFrom();
                LocalDateTime scheduledTimeTo = examInstance.getGeneratedLink().getScheduledTimeTo();

                if (examInstance.getTakenBy().equals(studentId)
                        && examInstance.getStatus().equals("assigned")
                        && scheduledTimeFrom.isAfter(LocalDateTime.now())
                        && scheduledTimeTo.isAfter(scheduledTimeFrom)) {
                    // add to current and unfinished exams
                    currentAndUnfinishedExams.add(examInstance);
                }
            }

            return currentAndUnfinishedExams;
        } catch (Exception e) {
            LOGGER.error("An error occurred while getting current and unfinished exams for student {}: {}", studentId, e.getMessage());
            throw new ExamException("An error occurred while getting current and unfinished exams for student " + studentId);
        }
    }

    // Run every hour
    @Scheduled(cron = "0 0 * * * *")
    public void remindStudents() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledTimeTo = now.plusHours(24);

        List<ExamInstance> exams = examInstanceRepository.findByGeneratedLinkScheduledTimeToBetween(now, scheduledTimeTo);
        for (ExamInstance exam : exams) {
            if (exam.getStatus().equals("assigned")
                    && scheduledTimeTo.isAfter(LocalDateTime.now())
                    && scheduledTimeTo.minusMinutes(60).isBefore(LocalDateTime.now())) {
                // Send notification to external service
                String url = this.notificationServiceURL + "/receive-exam-warning";
                helperUtil.postObjectToNotifications(exam, url);
            }

            if (exam.getStatus().equals("assigned")
                    && scheduledTimeTo.isBefore(LocalDateTime.now())) {
                // update status to ABSENT and create exam summary
                exam.setStatus("ABSENT");
                helperUtil.createExamSummary(exam, 0.0F);
            }
        }
    }

    public Set<ExamInstance> getAllExamInstancesForStudent(String studentId) throws ExamException {
        return helperUtil.getAllExamInstancesForStudent(studentId);
    }
}
