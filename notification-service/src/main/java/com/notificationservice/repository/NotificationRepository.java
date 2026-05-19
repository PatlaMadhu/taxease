package com.notificationservice.repository;

import com.notificationservice.entity.Notification;
import com.notificationservice.entity.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
    List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId);
    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);
}
