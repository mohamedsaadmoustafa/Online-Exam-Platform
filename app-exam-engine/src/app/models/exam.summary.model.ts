export interface ExamSummaryModel {
  id: string;
  userId: string;
  examInstanceId: number;
  score: number;
  status: StatusType;
}

enum StatusType {
  PASSED = 'PASSED',
  FAILED = 'FAILED',
  ABSENT = 'ABSENT'
}
