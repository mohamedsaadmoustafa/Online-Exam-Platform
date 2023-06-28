package com.example.questions.util;

import com.example.questions.controller.QuestionController;
import com.example.questions.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class HelperUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
    private final MongoTemplate mongoTemplate;

    @Autowired
    public HelperUtil(
            MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Question> getQuestionsByCategory(Integer levelId, String category, String subCategory) throws Exception {
        try {
            Query query = new Query();
            if (levelId!= null) {
                query.addCriteria(Criteria.where("level_id").is(levelId));
            }
            if (category!=null) {
                query.addCriteria(Criteria.where("category").is(category));
            }
            if (subCategory!=null) {
                query.addCriteria(Criteria.where("sub_category").is(subCategory));
            }
            return mongoTemplate.find(query, Question.class);
        } catch (Exception e) {
            throw new Exception("Error getting questions by category: " + e.getMessage());
        }
    }

    public Optional<Question> getQuestionById(String id) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            query.fields().include("name").include("expectedTime").include("answers");
            return Optional.ofNullable(mongoTemplate.findOne(query, Question.class));
        } catch (Exception e) {
            LOGGER.warn("Error getting question by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Question> getCorrectAnswerIdsForQuestionById(String id) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            query.fields().include("correctAnswerIds");
            return Optional.ofNullable(mongoTemplate.findOne(query, Question.class));
        } catch (Exception e) {
            LOGGER.warn("Error getting correct answer ids for question by id: " + e.getMessage());
        }
        return Optional.empty();
    }


    public List<Question> getQuestionsByIds(List<String> ids) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").in(ids));
            query.fields().include("name").include("expectedTime").include("answers");
            return mongoTemplate.find(query, Question.class);
        } catch (Exception e) {
            LOGGER.warn("Error getting questions by category: " + e.getMessage());
        }
        return null;
    }

    public List<Question> getCorrectAnswerIdsForQuestionsByIds(List<String> ids) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").in(ids));
            query.fields().include("correctAnswerIds");
            return mongoTemplate.find(query, Question.class);
        } catch (Exception e) {
            LOGGER.warn("Error getting correct Answer Ids: " + e.getMessage());
        }
        return null;
    }

}
