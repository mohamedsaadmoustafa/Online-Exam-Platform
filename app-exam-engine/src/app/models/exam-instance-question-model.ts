import {AnswerModel} from "./answer-model";
import {LocalDateTime} from "@js-joda/core";

export interface ExamInstanceQuestionModel {
  questionId: string;
  selectedAnswerIds: string[] | null;
  displayTime: LocalDateTime;
  answerTime: LocalDateTime;
}
