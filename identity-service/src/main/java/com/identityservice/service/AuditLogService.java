package com.identityservice.service;

import com.identityservice.dao.AuditLogRepository;
import com.identityservice.dao.UserRepository;
import com.identityservice.entity.AuditLog;
import com.identityservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public void record(String action, String resource) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return;
        }
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + auth.getName()));
        auditLogRepository.save(AuditLog.builder()
                .user(user).action(action).resource(resource).build());
    }

    public void recordRegistration(User newUser, String action, String resource) {
        auditLogRepository.save(AuditLog.builder()
                .user(newUser).action(action).resource(resource).build());
    }

    public List<AuditLog> list() {
        return auditLogRepository.findAll();
    }

    public AuditLog get(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("AuditLog not found: " + id));
    }
}
