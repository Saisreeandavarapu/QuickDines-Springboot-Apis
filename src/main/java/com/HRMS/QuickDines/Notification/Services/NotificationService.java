package com.HRMS.QuickDines.Notification.Services;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Notification.model.*;
import com.HRMS.QuickDines.Notification.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final PushNotificationRepository pushNotificationRepository;
    private final SmsNotificationRepository smsNotificationRepository;
    private final WhatsappNotificationRepository whatsappNotificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationLogRepository notificationLogRepository;

    private final EmployeeRepository employeeRepository;


    //=========================================================
    // NOTIFICATIONS
    //=========================================================

    public String createNotification(
            String employeeId,
            Notification notification) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        notification.setEmployee(employee);

        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }

        if (notification.getStatus() == null) {
            notification.setStatus("ACTIVE");
        }

        notificationRepository.save(notification);

        return "Notification Created Successfully";
    }


    public List<Notification> getNotifications() {

        return notificationRepository.findAll();
    }


    public Notification getNotification(Long id) {

        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification Not Found"));
    }


    public List<Notification> getEmployeeNotifications(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return notificationRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public String updateNotification(
            Long id,
            Notification notification) {

        Notification existing =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        existing.setTitle(notification.getTitle());
        existing.setMessage(notification.getMessage());
        existing.setNotificationType(
                notification.getNotificationType());
        existing.setPriority(notification.getPriority());
        existing.setIsRead(notification.getIsRead());
        existing.setStatus(notification.getStatus());

        notificationRepository.save(existing);

        return "Notification Updated Successfully";
    }


    public String deleteNotification(Long id) {

        Notification existing =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        notificationRepository.delete(existing);

        return "Notification Deleted Successfully";
    }


    public String markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        notification.setIsRead(true);

        notificationRepository.save(notification);

        return "Notification Marked As Read";
    }


    public String archiveNotification(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        notification.setStatus("ARCHIVED");

        notificationRepository.save(notification);

        return "Notification Archived Successfully";
    }


    //=========================================================
    // EMAIL NOTIFICATIONS
    //=========================================================

    public String createEmailNotification(
            Long notificationId,
            String employeeId,
            EmailNotification emailNotification) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        emailNotification.setNotification(notification);
        emailNotification.setEmployee(employee);

        if (emailNotification.getEmailStatus() == null) {
            emailNotification.setEmailStatus("PENDING");
        }

        if (emailNotification.getEmailAddress() == null) {
            emailNotification.setEmailAddress(
                    employee.getEmail());
        }

        emailNotificationRepository.save(emailNotification);

        return "Email Notification Created Successfully";
    }


    public List<EmailNotification> getEmailNotifications() {

        return emailNotificationRepository.findAll();
    }


    public EmailNotification getEmailNotification(Long id) {

        return emailNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Email Notification Not Found"));
    }


    public String updateEmailNotification(
            Long id,
            EmailNotification emailNotification) {

        EmailNotification existing =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email Notification Not Found"));

        existing.setEmailAddress(
                emailNotification.getEmailAddress());

        existing.setSubject(
                emailNotification.getSubject());

        existing.setEmailStatus(
                emailNotification.getEmailStatus());

        existing.setSentAt(
                emailNotification.getSentAt());

        emailNotificationRepository.save(existing);

        return "Email Notification Updated Successfully";
    }


    public String deleteEmailNotification(Long id) {

        EmailNotification existing =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email Notification Not Found"));

        emailNotificationRepository.delete(existing);

        return "Email Notification Deleted Successfully";
    }


    //=========================================================
    // PUSH NOTIFICATIONS
    //=========================================================

    public String createPushNotification(
            Long notificationId,
            String employeeId,
            PushNotification pushNotification) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        pushNotification.setNotification(notification);
        pushNotification.setEmployee(employee);

        if (pushNotification.getPushStatus() == null) {
            pushNotification.setPushStatus("PENDING");
        }

        pushNotificationRepository.save(pushNotification);

        return "Push Notification Created Successfully";
    }


    public List<PushNotification> getPushNotifications() {

        return pushNotificationRepository.findAll();
    }


    public PushNotification getPushNotification(Long id) {

        return pushNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Push Notification Not Found"));
    }


    public String updatePushNotification(
            Long id,
            PushNotification pushNotification) {

        PushNotification existing =
                pushNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Push Notification Not Found"));

        existing.setDeviceToken(
                pushNotification.getDeviceToken());

        existing.setPlatform(
                pushNotification.getPlatform());

        existing.setPushStatus(
                pushNotification.getPushStatus());

        existing.setSentAt(
                pushNotification.getSentAt());

        pushNotificationRepository.save(existing);

        return "Push Notification Updated Successfully";
    }


    public String deletePushNotification(Long id) {

        PushNotification existing =
                pushNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Push Notification Not Found"));

        pushNotificationRepository.delete(existing);

        return "Push Notification Deleted Successfully";
    }


    //=========================================================
    // SMS NOTIFICATIONS
    //=========================================================

    public String createSmsNotification(
            Long notificationId,
            String employeeId,
            SmsNotification smsNotification) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        smsNotification.setNotification(notification);
        smsNotification.setEmployee(employee);

        if (smsNotification.getDeliveryStatus() == null) {
            smsNotification.setDeliveryStatus("PENDING");
        }

        if (smsNotification.getMobileNumber() == null) {
            smsNotification.setMobileNumber(
                    employee.getMobileNumber());
        }

        smsNotificationRepository.save(smsNotification);

        return "SMS Notification Created Successfully";
    }


    public List<SmsNotification> getSmsNotifications() {

        return smsNotificationRepository.findAll();
    }


    public SmsNotification getSmsNotification(Long id) {

        return smsNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "SMS Notification Not Found"));
    }


    public String updateSmsNotification(
            Long id,
            SmsNotification smsNotification) {

        SmsNotification existing =
                smsNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SMS Notification Not Found"));

        existing.setMobileNumber(
                smsNotification.getMobileNumber());

        existing.setSmsMessage(
                smsNotification.getSmsMessage());

        existing.setProviderName(
                smsNotification.getProviderName());

        existing.setDeliveryStatus(
                smsNotification.getDeliveryStatus());

        existing.setSentAt(
                smsNotification.getSentAt());

        smsNotificationRepository.save(existing);

        return "SMS Notification Updated Successfully";
    }


    public String deleteSmsNotification(Long id) {

        SmsNotification existing =
                smsNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SMS Notification Not Found"));

        smsNotificationRepository.delete(existing);

        return "SMS Notification Deleted Successfully";
    }


    //=========================================================
    // WHATSAPP NOTIFICATIONS
    //=========================================================

    public String createWhatsappNotification(
            Long notificationId,
            String employeeId,
            WhatsappNotification whatsappNotification) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        whatsappNotification.setNotification(notification);
        whatsappNotification.setEmployee(employee);

        if (whatsappNotification.getDeliveryStatus() == null) {
            whatsappNotification.setDeliveryStatus("PENDING");
        }

        if (whatsappNotification.getMobileNumber() == null) {
            whatsappNotification.setMobileNumber(
                    employee.getMobileNumber());
        }

        whatsappNotificationRepository.save(
                whatsappNotification);

        return "WhatsApp Notification Created Successfully";
    }


    public List<WhatsappNotification> getWhatsappNotifications() {

        return whatsappNotificationRepository.findAll();
    }


    public WhatsappNotification getWhatsappNotification(Long id) {

        return whatsappNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "WhatsApp Notification Not Found"));
    }


    public String updateWhatsappNotification(
            Long id,
            WhatsappNotification whatsappNotification) {

        WhatsappNotification existing =
                whatsappNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "WhatsApp Notification Not Found"));

        existing.setMobileNumber(
                whatsappNotification.getMobileNumber());

        existing.setTemplateName(
                whatsappNotification.getTemplateName());

        existing.setMessage(
                whatsappNotification.getMessage());

        existing.setMediaUrl(
                whatsappNotification.getMediaUrl());

        existing.setDeliveryStatus(
                whatsappNotification.getDeliveryStatus());

        existing.setSentAt(
                whatsappNotification.getSentAt());

        whatsappNotificationRepository.save(existing);

        return "WhatsApp Notification Updated Successfully";
    }


    public String deleteWhatsappNotification(Long id) {

        WhatsappNotification existing =
                whatsappNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "WhatsApp Notification Not Found"));

        whatsappNotificationRepository.delete(existing);

        return "WhatsApp Notification Deleted Successfully";
    }


    //=========================================================
    // NOTIFICATION TEMPLATES
    //=========================================================

    public String createTemplate(
            NotificationTemplate template) {

        if (template.getStatus() == null) {
            template.setStatus("ACTIVE");
        }

        notificationTemplateRepository.save(template);

        return "Notification Template Created Successfully";
    }


    public List<NotificationTemplate> getTemplates() {

        return notificationTemplateRepository.findAll();
    }


    public NotificationTemplate getTemplate(Long id) {

        return notificationTemplateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification Template Not Found"));
    }


    public String updateTemplate(
            Long id,
            NotificationTemplate template) {

        NotificationTemplate existing =
                notificationTemplateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Template Not Found"));

        existing.setTemplateName(
                template.getTemplateName());

        existing.setTemplateCode(
                template.getTemplateCode());

        existing.setNotificationChannel(
                template.getNotificationChannel());

        existing.setSubject(
                template.getSubject());

        existing.setMessageTemplate(
                template.getMessageTemplate());

        existing.setVariables(
                template.getVariables());

        existing.setStatus(
                template.getStatus());

        notificationTemplateRepository.save(existing);

        return "Notification Template Updated Successfully";
    }


    public String deleteTemplate(Long id) {

        NotificationTemplate existing =
                notificationTemplateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Template Not Found"));

        notificationTemplateRepository.delete(existing);

        return "Notification Template Deleted Successfully";
    }


    //=========================================================
    // NOTIFICATION LOGS
    //=========================================================

    public String createNotificationLog(
            Long notificationId,
            String employeeId,
            NotificationLog notificationLog) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        notificationLog.setNotification(notification);
        notificationLog.setEmployee(employee);

        if (notificationLog.getDeliveryStatus() == null) {
            notificationLog.setDeliveryStatus("PENDING");
        }

        notificationLogRepository.save(notificationLog);

        return "Notification Log Created Successfully";
    }


    public List<NotificationLog> getNotificationLogs() {

        return notificationLogRepository.findAll();
    }


    public NotificationLog getNotificationLog(Long id) {

        return notificationLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification Log Not Found"));
    }


    public List<NotificationLog> getEmployeeNotificationLogs(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Not Found"));

        return notificationLogRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public String updateNotificationLog(
            Long id,
            NotificationLog notificationLog) {

        NotificationLog existing =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Log Not Found"));

        existing.setTemplate(
                notificationLog.getTemplate());

        existing.setChannel(
                notificationLog.getChannel());

        existing.setDeliveryStatus(
                notificationLog.getDeliveryStatus());

        existing.setErrorMessage(
                notificationLog.getErrorMessage());

        existing.setSentAt(
                notificationLog.getSentAt());

        existing.setDeliveredAt(
                notificationLog.getDeliveredAt());

        notificationLogRepository.save(existing);

        return "Notification Log Updated Successfully";
    }


    public String deleteNotificationLog(Long id) {

        NotificationLog existing =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Log Not Found"));

        notificationLogRepository.delete(existing);

        return "Notification Log Deleted Successfully";
    }

}