package com.HRMS.QuickDines.Notification.Controller;

import com.HRMS.QuickDines.Notification.Services.NotificationService;
import com.HRMS.QuickDines.Notification.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;


    //=========================================================
    // NOTIFICATIONS
    //=========================================================

    @PostMapping("/{employeeId}")
    public ResponseEntity<?> createNotification(
            @PathVariable String employeeId,
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                service.createNotification(employeeId, notification));
    }


    @GetMapping
    public ResponseEntity<?> getNotifications() {

        return ResponseEntity.ok(
                service.getNotifications());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getNotification(id));
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeNotifications(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeNotifications(employeeId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long id,
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                service.updateNotification(id, notification));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteNotification(id));
    }


    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.markAsRead(id));
    }


    @PutMapping("/{id}/archive")
    public ResponseEntity<?> archiveNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.archiveNotification(id));
    }


    //=========================================================
    // EMAIL NOTIFICATIONS
    //=========================================================

    @PostMapping("/email/{notificationId}/{employeeId}")
    public ResponseEntity<?> createEmailNotification(
            @PathVariable Long notificationId,
            @PathVariable String employeeId,
            @RequestBody EmailNotification emailNotification) {

        return ResponseEntity.ok(
                service.createEmailNotification(
                        notificationId,
                        employeeId,
                        emailNotification));
    }


    @GetMapping("/emails")
    public ResponseEntity<?> getEmailNotifications() {

        return ResponseEntity.ok(
                service.getEmailNotifications());
    }


    @GetMapping("/email/{id}")
    public ResponseEntity<?> getEmailNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEmailNotification(id));
    }


    @PutMapping("/email/{id}")
    public ResponseEntity<?> updateEmailNotification(
            @PathVariable Long id,
            @RequestBody EmailNotification emailNotification) {

        return ResponseEntity.ok(
                service.updateEmailNotification(
                        id,
                        emailNotification));
    }


    @DeleteMapping("/email/{id}")
    public ResponseEntity<?> deleteEmailNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteEmailNotification(id));
    }


    //=========================================================
    // PUSH NOTIFICATIONS
    //=========================================================

    @PostMapping("/push/{notificationId}/{employeeId}")
    public ResponseEntity<?> createPushNotification(
            @PathVariable Long notificationId,
            @PathVariable String employeeId,
            @RequestBody PushNotification pushNotification) {

        return ResponseEntity.ok(
                service.createPushNotification(
                        notificationId,
                        employeeId,
                        pushNotification));
    }


    @GetMapping("/pushes")
    public ResponseEntity<?> getPushNotifications() {

        return ResponseEntity.ok(
                service.getPushNotifications());
    }


    @GetMapping("/push/{id}")
    public ResponseEntity<?> getPushNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPushNotification(id));
    }


    @PutMapping("/push/{id}")
    public ResponseEntity<?> updatePushNotification(
            @PathVariable Long id,
            @RequestBody PushNotification pushNotification) {

        return ResponseEntity.ok(
                service.updatePushNotification(
                        id,
                        pushNotification));
    }


    @DeleteMapping("/push/{id}")
    public ResponseEntity<?> deletePushNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePushNotification(id));
    }


    //=========================================================
    // SMS NOTIFICATIONS
    //=========================================================

    @PostMapping("/sms/{notificationId}/{employeeId}")
    public ResponseEntity<?> createSmsNotification(
            @PathVariable Long notificationId,
            @PathVariable String employeeId,
            @RequestBody SmsNotification smsNotification) {

        return ResponseEntity.ok(
                service.createSmsNotification(
                        notificationId,
                        employeeId,
                        smsNotification));
    }


    @GetMapping("/sms")
    public ResponseEntity<?> getSmsNotifications() {

        return ResponseEntity.ok(
                service.getSmsNotifications());
    }


    @GetMapping("/sms/{id}")
    public ResponseEntity<?> getSmsNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSmsNotification(id));
    }


    @PutMapping("/sms/{id}")
    public ResponseEntity<?> updateSmsNotification(
            @PathVariable Long id,
            @RequestBody SmsNotification smsNotification) {

        return ResponseEntity.ok(
                service.updateSmsNotification(
                        id,
                        smsNotification));
    }


    @DeleteMapping("/sms/{id}")
    public ResponseEntity<?> deleteSmsNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSmsNotification(id));
    }


    //=========================================================
    // WHATSAPP NOTIFICATIONS
    //=========================================================

    @PostMapping("/whatsapp/{notificationId}/{employeeId}")
    public ResponseEntity<?> createWhatsappNotification(
            @PathVariable Long notificationId,
            @PathVariable String employeeId,
            @RequestBody WhatsappNotification whatsappNotification) {

        return ResponseEntity.ok(
                service.createWhatsappNotification(
                        notificationId,
                        employeeId,
                        whatsappNotification));
    }


    @GetMapping("/whatsapps")
    public ResponseEntity<?> getWhatsappNotifications() {

        return ResponseEntity.ok(
                service.getWhatsappNotifications());
    }


    @GetMapping("/whatsapp/{id}")
    public ResponseEntity<?> getWhatsappNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getWhatsappNotification(id));
    }


    @PutMapping("/whatsapp/{id}")
    public ResponseEntity<?> updateWhatsappNotification(
            @PathVariable Long id,
            @RequestBody WhatsappNotification whatsappNotification) {

        return ResponseEntity.ok(
                service.updateWhatsappNotification(
                        id,
                        whatsappNotification));
    }


    @DeleteMapping("/whatsapp/{id}")
    public ResponseEntity<?> deleteWhatsappNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteWhatsappNotification(id));
    }


    //=========================================================
    // NOTIFICATION TEMPLATES
    //=========================================================

    @PostMapping("/template")
    public ResponseEntity<?> createTemplate(
            @RequestBody NotificationTemplate template) {

        return ResponseEntity.ok(
                service.createTemplate(template));
    }


    @GetMapping("/templates")
    public ResponseEntity<?> getTemplates() {

        return ResponseEntity.ok(
                service.getTemplates());
    }


    @GetMapping("/template/{id}")
    public ResponseEntity<?> getTemplate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTemplate(id));
    }


    @PutMapping("/template/{id}")
    public ResponseEntity<?> updateTemplate(
            @PathVariable Long id,
            @RequestBody NotificationTemplate template) {

        return ResponseEntity.ok(
                service.updateTemplate(id, template));
    }


    @DeleteMapping("/template/{id}")
    public ResponseEntity<?> deleteTemplate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTemplate(id));
    }


    //=========================================================
    // NOTIFICATION LOGS
    //=========================================================

    @PostMapping("/log/{notificationId}/{employeeId}")
    public ResponseEntity<?> createNotificationLog(
            @PathVariable Long notificationId,
            @PathVariable String employeeId,
            @RequestBody NotificationLog notificationLog) {

        return ResponseEntity.ok(
                service.createNotificationLog(
                        notificationId,
                        employeeId,
                        notificationLog));
    }


    @GetMapping("/logs")
    public ResponseEntity<?> getNotificationLogs() {

        return ResponseEntity.ok(
                service.getNotificationLogs());
    }


    @GetMapping("/log/{id}")
    public ResponseEntity<?> getNotificationLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getNotificationLog(id));
    }


    @GetMapping("/logs/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeNotificationLogs(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeNotificationLogs(employeeId));
    }


    @PutMapping("/log/{id}")
    public ResponseEntity<?> updateNotificationLog(
            @PathVariable Long id,
            @RequestBody NotificationLog notificationLog) {

        return ResponseEntity.ok(
                service.updateNotificationLog(
                        id,
                        notificationLog));
    }


    @DeleteMapping("/log/{id}")
    public ResponseEntity<?> deleteNotificationLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteNotificationLog(id));
    }

}