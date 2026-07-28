package com.HRMS.QuickDines.Notification.Controller;

import com.HRMS.QuickDines.Notification.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;


    //=================================
    // NOTIFICATIONS
    //=================================

    @PostMapping("/notification/{employeeId}")
    public ResponseEntity<?> createNotification(@PathVariable String employeeId) {
        return ResponseEntity.ok(
                service.createNotification(employeeId));
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications() {
        return ResponseEntity.ok(
                service.getNotifications());
    }

    @GetMapping("/notification/{id}")
    public ResponseEntity<?> getNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.getNotification(id));
    }

    @PutMapping("/notification/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.updateNotification(id));
    }

    @DeleteMapping("/notification/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.deleteNotification(id));
    }


    //=================================
    // EMAIL NOTIFICATIONS
    //=================================

    @PostMapping("/email/{employeeId}")
    public ResponseEntity<?> createEmailNotification(@PathVariable String employeeId) {
        return ResponseEntity.ok(
                service.createEmailNotification(employeeId));
    }

    @GetMapping("/emails")
    public ResponseEntity<?> getEmailNotifications() {
        return ResponseEntity.ok(
                service.getEmailNotifications());
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<?> getEmailNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.getEmailNotification(id));
    }

    @PutMapping("/email/{id}")
    public ResponseEntity<?> updateEmailNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.updateEmailNotification(id));
    }

    @DeleteMapping("/email/{id}")
    public ResponseEntity<?> deleteEmailNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.deleteEmailNotification(id));
    }


    //=================================
    // PUSH NOTIFICATIONS
    //=================================

    @PostMapping("/push/{employeeId}")
    public ResponseEntity<?> createPushNotification(@PathVariable String employeeId) {
        return ResponseEntity.ok(
                service.createPushNotification(employeeId));
    }

    @GetMapping("/pushes")
    public ResponseEntity<?> getPushNotifications() {
        return ResponseEntity.ok(
                service.getPushNotifications());
    }

    @GetMapping("/push/{id}")
    public ResponseEntity<?> getPushNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.getPushNotification(id));
    }

    @PutMapping("/push/{id}")
    public ResponseEntity<?> updatePushNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.updatePushNotification(id));
    }

    @DeleteMapping("/push/{id}")
    public ResponseEntity<?> deletePushNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.deletePushNotification(id));
    }


    //=================================
    // REPORTS
    //=================================

    @GetMapping("/notifications/read")
    public ResponseEntity<?> readNotifications() {
        return ResponseEntity.ok(
                service.readNotifications());
    }

    @GetMapping("/notifications/unread")
    public ResponseEntity<?> unreadNotifications() {
        return ResponseEntity.ok(
                service.unreadNotifications());
    }

    @GetMapping("/emails/sent")
    public ResponseEntity<?> sentEmails() {
        return ResponseEntity.ok(
                service.sentEmails());
    }

    @GetMapping("/emails/pending")
    public ResponseEntity<?> pendingEmails() {
        return ResponseEntity.ok(
                service.pendingEmails());
    }

    @GetMapping("/push/sent")
    public ResponseEntity<?> sentPushNotifications() {
        return ResponseEntity.ok(
                service.sentPushNotifications());
    }

    @GetMapping("/push/pending")
    public ResponseEntity<?> pendingPushNotifications() {
        return ResponseEntity.ok(
                service.pendingPushNotifications());
    }


    //=================================
    // DASHBOARD COUNTS
    //=================================

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {
        return ResponseEntity.ok(
                service.getCounts());
    }

}