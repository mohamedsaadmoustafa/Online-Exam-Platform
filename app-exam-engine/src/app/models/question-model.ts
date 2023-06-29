import {AnswerModel} from "./answer-model";

export interface QuestionModel {
  id: string;
  name: string;
  levelId: number;
  category: string;
  subCategory: any;
  mark: number;
  expectedTime: number;
  createdBy: string;
  createdAt: string;
  correctAnswerIds: string[];
  answers: AnswerModel[];
}
