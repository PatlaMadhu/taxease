package com.notificationservice.service.impl;

import com.notificationservice.dto.NotificationRequest;
import com.notificationservice.dto.NotificationResponse;
import com.notificationservice.entity.Notification;
import com.notificationservice.entity.enums.NotificationCategory;
import com.notificationservice.entity.enums.NotificationStatus;
import com.notificationservice.repository.NotificationRepository;
import com.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        log.info("Sending notification to userId: {}", request.getUserId());
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .entityId(request.getEntityId())
                .message(request.getMessage())
                .category(request.getCategory())
                .status(NotificationStatus.UNREAD)
                .build();
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdInOrderByCreatedDateDesc(List.of(userId, 0L))
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<NotificationResponse> getUnreadByUser(Long userId) {
        return notificationRepository.findByUserIdInAndStatus(List.of(userId, 0L), NotificationStatus.UNREAD)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .filter(n -> n.getUserId().equals(userId) || n.getUserId().equals(0L))
                .orElseThrow(() -> new NoSuchElementException(
                        "Notification not found or does not belong to user: " + userId));
        if (notification.getStatus() == NotificationStatus.READ) {
            return toResponse(notification);
        }
        notification.setStatus(NotificationStatus.READ);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void broadcastNotification(String message, NotificationCategory category) {
        log.info("Broadcasting notification to all users, category: {}", category);
        Notification notification = Notification.builder()
                .userId(0L) // 0L = broadcast sentinel — visible to ALL users
                .message(message)
                .category(category != null ? category : NotificationCategory.BROADCAST)
                .status(NotificationStatus.UNREAD)
                .build();
        notificationRepository.save(notification);
        log.info("Broadcast notification saved");
    }

    @Override
    @Transactional
    public void sendNotificationToUser(Long userId, String message, NotificationCategory category) {
        log.info("Sending notification to userId: {}", userId);
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .category(category != null ? category : NotificationCategory.SYSTEM_UPDATE)
                .status(NotificationStatus.UNREAD)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getByCategory(Long userId, NotificationCategory category) {
        return notificationRepository.findByUserIdInAndCategoryOrderByCreatedDateDesc(List.of(userId, 0L), category)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void sendDeadlineAlert(Long userId, String period, String deadlineDate) {
        log.info("Sending deadline alert to userId: {} for period: {}", userId, period);
        Notification notification = Notification.builder()
                .userId(userId)
                .message("Reminder: Your tax filing for period " + period + " is due on " + deadlineDate + ". Please submit before the deadline.")
                .category(NotificationCategory.DEADLINE_ALERT)
                .status(NotificationStatus.UNREAD)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void broadcastProgramUpdate(String title, String details) {
        log.info("Broadcasting program update: {}", title);
        Notification notification = Notification.builder()
                .userId(0L)
                .message(title + ": " + details)
                .category(NotificationCategory.PROGRAM_UPDATE)
                .status(NotificationStatus.UNREAD)
                .build();
        notificationRepository.save(notification);
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getId())
                .userId(n.getUserId())
                .entityId(n.getEntityId())
                .message(n.getMessage())
                .category(n.getCategory())
                .status(n.getStatus())
                .createdDate(n.getCreatedDate())
                .build();
    }
}
