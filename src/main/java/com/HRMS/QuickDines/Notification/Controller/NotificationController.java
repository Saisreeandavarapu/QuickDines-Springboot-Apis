package com.HRMS.QuickDines.Notification.Controller;

import com.HRMS.QuickDines.Notification.Services.NotificationService;
import com.HRMS.QuickDines.Notification.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('NOTIFICATION_CREATE')")
    public ResponseEntity<?> createNotification(
            @PathVariable String employeeId,
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                service.createNotification(employeeId, notification));
    }


    @GetMapping
    @PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
    public ResponseEntity<?> getNotifications() {

        return ResponseEntity.ok(
                service.getNotifications());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
    public ResponseEntity<?> getNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getNotification(id));
    }


    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
    public ResponseEntity<?> getEmployeeNotifications(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeNotifications(employeeId));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_UPDATE')")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long id,
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                service.updateNotification(id, notification));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_DELETE')")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteNotification(id));
    }


    @PutMapping("/{id}/read")
    @PreAuthorize("hasAuthority('NOTIFICATION_MARK_READ')")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.markAsRead(id));
    }


    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAuthority('NOTIFICATION_ARCHIVE')")
    public ResponseEntity<?> archiveNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.archiveNotification(id));
    }


    //=========================================================
    // EMAIL NOTIFICATIONS
    //=========================================================

    @PostMapping("/email/{notificationId}/{employeeId}")
    @PreAuthorize("hasAuthority('EMAIL_NOTIFICATION_CREATE')")
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
    @PreAuthorize("hasAuthority('EMAIL_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getEmailNotifications() {

        return ResponseEntity.ok(
                service.getEmailNotifications());
    }


    @GetMapping("/email/{id}")
    @PreAuthorize("hasAuthority('EMAIL_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getEmailNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEmailNotification(id));
    }


    @PutMapping("/email/{id}")
    @PreAuthorize("hasAuthority('EMAIL_NOTIFICATION_UPDATE')")
    public ResponseEntity<?> updateEmailNotification(
            @PathVariable Long id,
            @RequestBody EmailNotification emailNotification) {

        return ResponseEntity.ok(
                service.updateEmailNotification(
                        id,
                        emailNotification));
    }


    @DeleteMapping("/email/{id}")
    @PreAuthorize("hasAuthority('EMAIL_NOTIFICATION_DELETE')")
    public ResponseEntity<?> deleteEmailNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteEmailNotification(id));
    }


    //=========================================================
    // PUSH NOTIFICATIONS
    //=========================================================

    @PostMapping("/push/{notificationId}/{employeeId}")
    @PreAuthorize("hasAuthority('PUSH_NOTIFICATION_CREATE')")
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
    @PreAuthorize("hasAuthority('PUSH_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getPushNotifications() {

        return ResponseEntity.ok(
                service.getPushNotifications());
    }


    @GetMapping("/push/{id}")
    @PreAuthorize("hasAuthority('PUSH_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getPushNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPushNotification(id));
    }


    @PutMapping("/push/{id}")
    @PreAuthorize("hasAuthority('PUSH_NOTIFICATION_UPDATE')")
    public ResponseEntity<?> updatePushNotification(
            @PathVariable Long id,
            @RequestBody PushNotification pushNotification) {

        return ResponseEntity.ok(
                service.updatePushNotification(
                        id,
                        pushNotification));
    }


    @DeleteMapping("/push/{id}")
    @PreAuthorize("hasAuthority('PUSH_NOTIFICATION_DELETE')")
    public ResponseEntity<?> deletePushNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePushNotification(id));
    }


    //=========================================================
    // SMS NOTIFICATIONS
    //=========================================================

    @PostMapping("/sms/{notificationId}/{employeeId}")
    @PreAuthorize("hasAuthority('SMS_NOTIFICATION_CREATE')")
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
    @PreAuthorize("hasAuthority('SMS_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getSmsNotifications() {

        return ResponseEntity.ok(
                service.getSmsNotifications());
    }


    @GetMapping("/sms/{id}")
    @PreAuthorize("hasAuthority('SMS_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getSmsNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSmsNotification(id));
    }


    @PutMapping("/sms/{id}")
    @PreAuthorize("hasAuthority('SMS_NOTIFICATION_UPDATE')")
    public ResponseEntity<?> updateSmsNotification(
            @PathVariable Long id,
            @RequestBody SmsNotification smsNotification) {

        return ResponseEntity.ok(
                service.updateSmsNotification(
                        id,
                        smsNotification));
    }


    @DeleteMapping("/sms/{id}")
    @PreAuthorize("hasAuthority('SMS_NOTIFICATION_DELETE')")
    public ResponseEntity<?> deleteSmsNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSmsNotification(id));
    }


    //=========================================================
    // WHATSAPP NOTIFICATIONS
    //=========================================================

    @PostMapping("/whatsapp/{notificationId}/{employeeId}")
    @PreAuthorize("hasAuthority('WHATSAPP_NOTIFICATION_CREATE')")
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
    @PreAuthorize("hasAuthority('WHATSAPP_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getWhatsappNotifications() {

        return ResponseEntity.ok(
                service.getWhatsappNotifications());
    }


    @GetMapping("/whatsapp/{id}")
    @PreAuthorize("hasAuthority('WHATSAPP_NOTIFICATION_VIEW')")
    public ResponseEntity<?> getWhatsappNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getWhatsappNotification(id));
    }


    @PutMapping("/whatsapp/{id}")
    @PreAuthorize("hasAuthority('WHATSAPP_NOTIFICATION_UPDATE')")
    public ResponseEntity<?> updateWhatsappNotification(
            @PathVariable Long id,
            @RequestBody WhatsappNotification whatsappNotification) {

        return ResponseEntity.ok(
                service.updateWhatsappNotification(
                        id,
                        whatsappNotification));
    }


    @DeleteMapping("/whatsapp/{id}")
    @PreAuthorize("hasAuthority('WHATSAPP_NOTIFICATION_DELETE')")
    public ResponseEntity<?> deleteWhatsappNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteWhatsappNotification(id));
    }


    //=========================================================
    // NOTIFICATION TEMPLATES
    //=========================================================

    @PostMapping("/template")
    @PreAuthorize("hasAuthority('NOTIFICATION_TEMPLATE_CREATE')")
    public ResponseEntity<?> createTemplate(
            @RequestBody NotificationTemplate template) {

        return ResponseEntity.ok(
                service.createTemplate(template));
    }


    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('NOTIFICATION_TEMPLATE_VIEW')")
    public ResponseEntity<?> getTemplates() {

        return ResponseEntity.ok(
                service.getTemplates());
    }


    @GetMapping("/template/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_TEMPLATE_VIEW')")
    public ResponseEntity<?> getTemplate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTemplate(id));
    }


    @PutMapping("/template/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_TEMPLATE_UPDATE')")
    public ResponseEntity<?> updateTemplate(
            @PathVariable Long id,
            @RequestBody NotificationTemplate template) {

        return ResponseEntity.ok(
                service.updateTemplate(id, template));
    }


    @DeleteMapping("/template/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_TEMPLATE_DELETE')")
    public ResponseEntity<?> deleteTemplate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTemplate(id));
    }


    //=========================================================
    // NOTIFICATION LOGS
    //=========================================================

    @PostMapping("/log/{notificationId}/{employeeId}")
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_CREATE')")
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
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_VIEW')")
    public ResponseEntity<?> getNotificationLogs() {

        return ResponseEntity.ok(
                service.getNotificationLogs());
    }


    @GetMapping("/log/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_VIEW')")
    public ResponseEntity<?> getNotificationLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getNotificationLog(id));
    }


    @GetMapping("/logs/employee/{employeeId}")
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_VIEW')")
    public ResponseEntity<?> getEmployeeNotificationLogs(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeNotificationLogs(employeeId));
    }


    @PutMapping("/log/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_UPDATE')")
    public ResponseEntity<?> updateNotificationLog(
            @PathVariable Long id,
            @RequestBody NotificationLog notificationLog) {

        return ResponseEntity.ok(
                service.updateNotificationLog(
                        id,
                        notificationLog));
    }


    @DeleteMapping("/log/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_LOG_DELETE')")
    public ResponseEntity<?> deleteNotificationLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteNotificationLog(id));
    }
}