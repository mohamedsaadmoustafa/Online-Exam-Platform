import {GeneratedLinkModel} from "./generated-link-model";
import {ExamInstanceQuestionModel} from "./exam-instance-question-model";

export interface ExamInstanceModel {
  id: any;
  examDefinitionId: number;
  startTime: Date | null;
  endTime: Date | null;
  durationInMinutes: number;
  assignedBy: string; // teacher username/id
  takenBy: string;   // student username/id
  generatedLink: GeneratedLinkModel;
  questions: ExamInstanceQuestionModel[] | null;
  status: string
}

