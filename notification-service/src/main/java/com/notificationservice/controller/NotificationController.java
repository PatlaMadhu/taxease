package com.notificationservice.controller;

import com.notificationservice.dto.BroadcastRequest;
import com.notificationservice.dto.DeadlineAlertRequest;
import com.notificationservice.dto.DirectNotificationRequest;
import com.notificationservice.dto.NotificationRequest;
import com.notificationservice.dto.NotificationResponse;
import com.notificationservice.dto.ProgramUpdateRequest;
import com.notificationservice.service.NotificationService;
import com.notificationservice.entity.enums.NotificationCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    private void requireAdmin(String role) {
        if (!"ADMINISTRATOR".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: only ADMINISTRATOR can send notifications");
        }
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody NotificationRequest request) {
        requireAdmin(role);
        log.info("Sending notification to user: {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.sendNotification(request));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody BroadcastRequest request) {
        requireAdmin(role);
        log.info("Broadcasting notification category: {} by ADMIN", request.getCategory());
        notificationService.broadcastNotification(request.getMessage(), request.getCategory());
        return ResponseEntity.ok("Broadcast notification sent successfully to all users.");
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> sendDirectNotification(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId,
            @Valid @RequestBody DirectNotificationRequest request) {
        requireAdmin(role);
        log.info("Admin sending direct notification to userId: {}", userId);
        notificationService.sendNotificationToUser(userId, request.getMessage(), request.getCategory());
        return ResponseEntity.ok("Notification sent successfully to user " + userId);
    }

    @PostMapping("/deadline-alert")
    public ResponseEntity<String> sendDeadlineAlert(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody DeadlineAlertRequest request) {
        requireAdmin(role);
        log.info("Admin sending deadline alert to userId: {}", request.getUserId());
        notificationService.sendDeadlineAlert(request.getUserId(), request.getPeriod(), request.getDeadlineDate());
        return ResponseEntity.ok("Deadline alert sent to user " + request.getUserId());
    }

    @PostMapping("/program-update")
    public ResponseEntity<String> broadcastProgramUpdate(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody ProgramUpdateRequest request) {
        requireAdmin(role);
        log.info("Admin broadcasting program update: {}", request.getTitle());
        notificationService.broadcastProgramUpdate(request.getTitle(), request.getDetails());
        return ResponseEntity.ok("Program update broadcast to all users.");
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestHeader("X-User-Id") String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);
        log.info("Fetching notifications for logged-in userId: {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/my/unread")
    public ResponseEntity<List<NotificationResponse>> getMyUnread(
            @RequestHeader("X-User-Id") String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);
        log.info("Fetching unread notifications for logged-in userId: {}", userId);
        return ResponseEntity.ok(notificationService.getUnreadByUser(userId));
    }

    @GetMapping("/my/category/{category}")
    public ResponseEntity<List<NotificationResponse>> getMyByCategory(
            @RequestHeader("X-User-Id") String userIdHeader,
            @PathVariable NotificationCategory category) {
        Long userId = Long.parseLong(userIdHeader);
        log.info("Fetching {} notifications for logged-in userId: {}", category, userId);
        return ResponseEntity.ok(notificationService.getByCategory(userId, category));
    }

    // Admin-only: fetch any user's notifications
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {
        requireAdmin(role);
        log.info("Admin fetching notifications for userId: {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {
        requireAdmin(role);
        return ResponseEntity.ok(notificationService.getUnreadByUser(userId));
    }

    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<NotificationResponse>> getByCategory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId,
            @PathVariable NotificationCategory category) {
        requireAdmin(role);
        log.info("Admin fetching {} notifications for userId: {}", category, userId);
        return ResponseEntity.ok(notificationService.getByCategory(userId, category));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("X-User-Id") String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);
        log.info("Marking notification {} as read for userId: {}", notificationId, userId);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok("Notification successfully marked as read.");
    }
}
