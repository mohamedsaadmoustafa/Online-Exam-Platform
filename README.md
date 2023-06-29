# Online Exam Platform
The Online Exam Platform is a comprehensive system for conducting online examinations. It consists of four microservices - Question Bank, Exam Engine, User Authentication, and Notification Management. These microservices are developed using Java, Spring Boot, Angular, and integrated with MongoDB, PostgreSQL, Keycloak, and Kafka.

### Project Overview
The Online Exam Platform provides the following functionalities:

* **Question Bank Service**: The Question Bank service manages questions that can be used in exams. It allows users to create, delete, update, and list questions. Each question has attributes such as ID, name, level, category, sub-category, mark, expected time, correct answer IDs, created by, created at, and answers.

* **Exam Engine Service**: The Exam Engine service enables teachers to create exam definitions, assign exams to students, and track their progress. An exam definition includes an ID, name, passing score, and a list of associated questions. Students can take exams, submit their answers, and receive results. Exam instances have attributes like ID, exam definition ID, start time, end time, duration, completion time, generated link, scheduled time range, token, URL, created by, taken by, status, and question-answer mappings.

* **User Authentication**: User authentication is handled by the User Authentication microservice, which utilizes Keycloak. It provides APIs for user creation, login, and signup.

* **Notification Management**: The Notification Management microservice handles user notifications. It uses Kafka to produce and consume notification messages. Notifications include information such as user ID, timestamp, URL, and message.

The platform also integrates Firebase for sending notifications to the Angular frontend application.

### Getting Started
To run the Online Exam Platform locally, follow these steps:

1. Clone the repository: git clone https://github.com/your/repository.git.
2. Install the necessary dependencies for each microservice.
3. Configure the environment variables for each microservice, including database connection details and Kafka configuration.
4. Build and run each microservice using the respective build tools (Maven for Java microservices, Angular CLI for frontend).
5. Ensure all microservices are running and communicating properly.
6. Access the platform through the provided URLs or endpoints.

### Directory Structure
The project directory structure is organized as follows:
```
- question-bank-microservice/
    - src/
    - pom.xml
    - ...
    
- exam-engine-microservice/
    - src/
    - pom.xml
    - ...
    
- user-microservice/
    - src/
    - pom.xml
    - ...
    
- notification-management-microservice/
    - src/
    - pom.xml
    - ...
    
- app-question-bank/
    - src/
    - angular.json
    - package.json
    - ...

  - app-exam-engine/
    - src/
    - angular.json
    - package.json
    - ...  

- README.md
- LICENSE
- ...
```

### License
This project is licensed under the MIT License.

