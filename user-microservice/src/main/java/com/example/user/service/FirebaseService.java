package com.example.user.service;

import com.example.user.controller.UserController;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Optional;

@Service
public class FirebaseService {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public FirebaseService(
            UserRepository userRepository,
            MongoTemplate mongoTemplate
    ) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void saveNewFirebaseToken(String username, String token) {
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().set("token", token);

        UpdateResult result = mongoTemplate.upsert(query, update, User.class);

        if (result.getModifiedCount() > 0 || result.getUpsertedId() != null) {
            LOG.info("Successfully updating/adding token for username: " + username);
        } else {
            LOG.warn("Failed to update/add token for username: " + username);
        }
    }

    public String getFirebaseToken(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = user.getToken();
            LOG.info("Token found for " + username);
            LOG.info(token);
            return token;
        } else {
            LOG.warn("Error finding username: " + username);
            return null;
        }
    }
}
