package com.notificationservice.controller;

import com.notificationservice.dto.BroadcastRequest;
import com.notificationservice.dto.DirectNotificationRequest;
import com.notificationservice.dto.NotificationRequest;
import com.notificationservice.dto.NotificationResponse;
import com.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Sending notification to user: {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.sendNotification(request));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody BroadcastRequest request) {
        log.info("Broadcasting notification category: {}", request.getCategory());
        notificationService.broadcastNotification(request.getMessage(), request.getCategory());
        return ResponseEntity.ok("Broadcast notification sent successfully to all users.");
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> sendDirectNotification(
            @PathVariable Long userId,
            @RequestBody DirectNotificationRequest request) {
        log.info("Sending direct notification to user: {}", userId);
        notificationService.sendNotificationToUser(userId, request.getMessage(), request.getCategory());
        return ResponseEntity.ok("Notification sent successfully to user " + userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Long userId) {
        log.info("Fetching notifications for user: {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadByUser(userId));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        log.info("Marking notification {} as read for user {}", notificationId, userId);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok("Notification successfully marked as read.");
    }
}
