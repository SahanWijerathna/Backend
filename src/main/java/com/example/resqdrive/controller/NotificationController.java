package com.example.resqdrive.controller;

import com.example.resqdrive.config.JwtFilter.CustomPrincipal;
import com.example.resqdrive.model.Notification;
import com.example.resqdrive.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Notification> notifs = notificationRepository.findByUserIdOrderByTimestampDesc(principal.getId());
        return ResponseEntity.ok(notifs);
    }

    @PutMapping("/read")
    public ResponseEntity<?> markAllRead() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Notification> notifs = notificationRepository.findByUserIdOrderByTimestampDesc(principal.getId());
        for (Notification n : notifs) {
            n.setRead(true);
            notificationRepository.save(n);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") String id) {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Optional<Notification> notifOpt = notificationRepository.findById(id);

        if (notifOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Notification not found."));
        }

        Notification notif = notifOpt.get();
        // Access control check
        if (!notif.getUserId().equals(principal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Forbidden access to notification."));
        }

        notificationRepository.delete(notif);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
