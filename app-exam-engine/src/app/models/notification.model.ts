export interface NotificationModel{
  id: number;
  message: string;
  userId: string;
  notificationType: NotificationType;
  notificationTime: Date;
  generatedLinkUrl: string;
}
enum NotificationType {
  EXAM_NOTIFICATION = 'EXAM_NOTIFICATION',
    EXAM_REMINDER = 'EXAM_REMINDER',
    EXAM_RESULT = 'EXAM_RESULT'
}

