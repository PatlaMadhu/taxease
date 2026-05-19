export interface NotificationResponse {
  notificationId: number;
  userId: number;
  entityId: number;
  message: string;
  category: string;
  status: 'UNREAD' | 'READ' | 'ARCHIVED';
  createdDate: string;
}

export interface NotificationRequest {
  userId: number;
  entityId?: number;
  message: string;
  category: 'FILING' | 'PAYMENT' | 'COMPLIANCE' | 'SYSTEM_UPDATE' | 'BROADCAST';
}

export interface BroadcastRequest {
  message: string;
  category: 'FILING' | 'PAYMENT' | 'COMPLIANCE' | 'SYSTEM_UPDATE' | 'BROADCAST';
}
