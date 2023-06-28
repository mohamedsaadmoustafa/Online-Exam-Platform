package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/")
//@CrossOrigin("${app.allowed.origins}")
//@CrossOrigin(origins = "https://localhost:4200")
@CrossOrigin
public class QuestionController {

    private final QuestionService questionService;
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    // Create new Question
    @PostMapping("/")
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    // List Question(Pagination) with sorting order based on 'createdAt' feature
    @GetMapping("/")
    public List<Question> getAllQuestions(@RequestParam(defaultValue = "1") int pageNo) {
        return questionService.getAllQuestionsWithPagination(pageNo);
    }

    // Update Question
    @PutMapping("/")
    public Question updateQuestion(@RequestBody Question question) {
        return questionService.updateQuestion(question);
    }

    // Add Answer for existing Question
    @PostMapping("/{questionId}/answer")
    public Question addAnswerToQuestion(@PathVariable String questionId,
                                        @RequestBody Question.Answer answer) {
        return questionService.addAnswerToQuestion(questionId, answer);
    }

    // Delete Question
    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable String id) {
        return questionService.deleteQuestion(id);
    }

    // Delete Answer for existing Question
    @DeleteMapping("/delete/{questionId}/answer/{answerId}")
    public String deleteAnswerFromQuestion(@PathVariable String questionId,
                                             @PathVariable String answerId) {
        return questionService.deleteAnswerFromQuestion(questionId, answerId);
    }

    // Get number of questions
    @GetMapping("/count")
    public long getNumberOfQuestions() throws Exception {
        return questionService.getNumberOfQuestions();
    }

    // filter questions by question features
    @GetMapping("/filter")
    public List<Question> getQuestionsByCategory(@RequestParam(name = "levelId", required = false) Integer levelId,
                                                 @RequestParam(name = "category", required = false) String category,
                                                 @RequestParam(name = "subCategory", required = false) String subCategory) throws Exception {
        return questionService.getQuestionsByCategory(levelId, category, subCategory);
    }

    @GetMapping("/questionsByIds")
    public List<Question> getQuestionsByIds(@RequestParam("ids") List<String> ids) {
        return questionService.getQuestionsByIds(ids);
    }

    @GetMapping("/questionsByIds-correctAnswerIds")
    public List<Question> getCorrectAnswerIdsForQuestionsByIds(@RequestParam("ids") List<String> ids) {
        return questionService.getCorrectAnswerIdsForQuestionsByIds(ids);
    }

    // get Question By id -> name, answers
    @GetMapping("/questions{questionId}")
    public Optional<Question> getQuestionById(@PathVariable String questionId) {
        return questionService.getQuestionById(questionId);
    }

    //  get correctAnswerIds for Question By id
    @GetMapping("/{questionId}/correctAnswerIds")
    public Optional<Question> getCorrectAnswerIdsForQuestionById(@PathVariable String questionId) {
        return questionService.getCorrectAnswerIdsForQuestionById(questionId);
    }

}