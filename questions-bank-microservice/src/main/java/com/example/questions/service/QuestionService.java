package com.example.questions.service;

import com.example.questions.model.Question;
import com.example.questions.repository.QuestionRepository;
import com.example.questions.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import lombok.NonNull;
import java.util.*;

@Service
public class QuestionService {
    private final HelperUtil helperUtil;
    private final QuestionRepository questionRepository;
    private final MongoTemplate mongoTemplate;
    private final int pageSize;
    private final String collectionName;

    @Autowired
    public QuestionService(
            HelperUtil helperUtil,
            QuestionRepository questionRepository,
            MongoTemplate mongoTemplate,
            @Value("${page.default-page-size}") int pageSize,
            @Value("${spring.data.mongodb.collection}") String collectionName
    ) {
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
        this.pageSize = pageSize;
        this.collectionName = collectionName;
        this.helperUtil = helperUtil;
    }

    public Question createQuestion(@NonNull Question question) {
        if (question.getName().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if (question.getCorrectAnswerIds().isEmpty()) {
            throw new IllegalArgumentException("At least one correct answer should be provided.");
        }
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestionsWithPagination(int pageNo) {
        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number should be greater than 0.");
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return questionRepository.findAll(pageable).getContent();
    }

    public Question updateQuestion(@NonNull Question question) {
        if (question.getName().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if (question.getCorrectAnswerIds().isEmpty()) {
            throw new IllegalArgumentException("At least one correct answer should be provided.");
        }
        return questionRepository.save(question);
    }

    public Question addAnswerToQuestion(String questionId, Question.Answer answer) {
        if (questionId.isEmpty()) {
            throw new IllegalArgumentException("Question ID parameter cannot be null or empty.");
        }
        if (answer == null) {
            throw new IllegalArgumentException("Answer parameter cannot be null.");
        }
        Question existingQuestion = questionRepository.findById(questionId).orElse(null);
        if (existingQuestion == null) {
            throw new IllegalArgumentException(String.format("Question with ID %s not found.", questionId));
        }
        existingQuestion.getAnswers().add(answer);
        return questionRepository.save(existingQuestion);
    }

    public String deleteQuestion(@NonNull String questionId) {
        if (questionId.isEmpty()) {
            throw new IllegalArgumentException("Question ID parameter cannot be null or empty.");
        }
        Optional<Question> existingQuestion = questionRepository.findById(questionId);
        if (existingQuestion.isEmpty()) {
            throw new IllegalArgumentException(String.format("Question with ID %s not found.", questionId));
        }
        questionRepository.deleteById(questionId);
        return String.format("Question with ID %s deleted successfully.", questionId);
    }

    public String deleteAnswerFromQuestion(@NonNull String questionId, @NonNull String answerId) {
        if (questionId.isEmpty()) {
            throw new IllegalArgumentException("Question ID parameter cannot be null or empty.");
        }
        if (answerId.isEmpty()) {
            throw new IllegalArgumentException("Answer ID parameter cannot be null or empty.");
        }
        Question existingQuestion = questionRepository.findById(questionId).orElse(null);
        if (existingQuestion == null) {
            throw new IllegalArgumentException(String.format("Question with ID %s not found.", questionId));
        }
        if (!existingQuestion.getCorrectAnswerIds().contains(answerId)) {
            throw new IllegalArgumentException(String.format("Answer with ID %s does not exist in Question with ID %s.", answerId, questionId));
        }
        if (existingQuestion.getCorrectAnswerIds().size() == 1) {
            throw new IllegalArgumentException("At least one correct ID should exist.");
        }
        existingQuestion.getAnswers().removeIf(answer -> answer.getId().equals(answerId));
        existingQuestion.getCorrectAnswerIds().remove(answerId);
        questionRepository.save(existingQuestion);
        return String.format("Answer with ID %s in Question with ID %s deleted successfully.", answerId, questionId);
    }

    public long getNumberOfQuestions() throws Exception {
        try {
            Query query = new Query();
            return mongoTemplate.count(query, collectionName);
        } catch (Exception e) {
            throw new Exception("Error getting number of questions: " + e.getMessage());
        }
    }

    // filter questions based on some collection features
    public List<Question> getQuestionsByCategory(Integer levelId, String category, String subCategory) throws Exception {
        return this.helperUtil.getQuestionsByCategory(levelId, category, subCategory);
    }

    public List<Question> getQuestionsByIds(List<String> ids) {
        return this.helperUtil.getQuestionsByIds(ids);
    }

    public List<Question> getCorrectAnswerIdsForQuestionsByIds(List<String> ids) {
        return this.helperUtil.getCorrectAnswerIdsForQuestionsByIds(ids);
    }

    // Replaced
    // get Question By id
    public Optional<Question> getQuestionById(String id) {
        return this.helperUtil.getQuestionById(id);
    }

    // get correctAnswerIds for Question By id
    public Optional<Question> getCorrectAnswerIdsForQuestionById(String id) {
        return this.helperUtil.getCorrectAnswerIdsForQuestionById(id);
    }

}