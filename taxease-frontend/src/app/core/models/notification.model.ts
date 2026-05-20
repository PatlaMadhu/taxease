export interface NotificationResponse {
  notificationId: number;
  userId: number;
  entityId: number;
  message: string;
  category: string;
  status: 'UNREAD' | 'READ' | 'ARCHIVED';
  createdDate: string;
}

export type NotificationCategory = 'FILING' | 'PAYMENT' | 'COMPLIANCE' | 'SYSTEM_UPDATE' | 'BROADCAST' | 'DEADLINE_ALERT' | 'PROGRAM_UPDATE';

export interface NotificationRequest {
  userId: number;
  entityId?: number;
  message: string;
  category: NotificationCategory;
}

export interface BroadcastRequest {
  message: string;
  category: NotificationCategory;
}
