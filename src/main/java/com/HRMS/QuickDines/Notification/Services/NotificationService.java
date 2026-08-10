package com.HRMS.QuickDines.Notification.Services;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Leave.model.LeaveType;
import com.HRMS.QuickDines.Notification.model.*;
import com.HRMS.QuickDines.Notification.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


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

        Notification saved =
                notificationRepository.save(notification);

        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "NOTIFICATION",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Notification Created Successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_NOTIFICATION",
                "NOTIFICATION",
                "Notification Created Successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION",
                "NotificationService",
                "Notification Created Successfully"
        );

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


    //=========================================================
// UPDATE NOTIFICATION
//=========================================================

    public String updateNotification(
            Long id,
            Notification notification) {

        Notification existing =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue =
                convertToJson(existing);

        existing.setTitle(notification.getTitle());

        existing.setMessage(notification.getMessage());

        existing.setNotificationType(
                notification.getNotificationType());

        existing.setPriority(
                notification.getPriority());

        existing.setIsRead(
                notification.getIsRead());

        existing.setStatus(
                notification.getStatus());

        Notification updated =
                notificationRepository.save(existing);

        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.logUpdate(
                "NOTIFICATION",
                String.valueOf(updated.getId()),
                performedBy,
                notification.getEmployee().getEmployeeId(),
                "Notification Updated Successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_NOTIFICATION",
                "NOTIFICATION",
                "Notification Updated Successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION",
                "NotificationService",
                "Notification Updated Successfully"
        );

        return "Notification Updated Successfully";
    }

    //=========================================================
// DELETE NOTIFICATION
//=========================================================

    public String deleteNotification(Long id) {

        Notification existing =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        notificationRepository.delete(existing);

        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.logDelete(
                "NOTIFICATION",
                String.valueOf(id),
                performedBy,
                deletedValue,
                "Notification Deleted Successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_NOTIFICATION",
                "NOTIFICATION",
                "Notification Deleted Successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION",
                "NotificationService",
                "Notification Deleted Successfully"
        );

        return "Notification Deleted Successfully";
    }


    //=========================================================
// MARK AS READ
//=========================================================

    public String markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        String oldValue =
                convertToJson(notification);

        notification.setIsRead(true);

        Notification updated =
                notificationRepository.save(notification);

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.logUpdate(
                "NOTIFICATION",
                String.valueOf(updated.getId()),
                performedBy,
                notification.getEmployee().getEmployeeId(),
                "Notification Marked As Read",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "MARK_NOTIFICATION_READ",
                "NOTIFICATION",
                "Notification Marked As Read",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION",
                "NotificationService",
                "Notification Marked As Read"
        );

        return "Notification Marked As Read";
    }


    //=========================================================
// ARCHIVE NOTIFICATION
//=========================================================

    public String archiveNotification(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Not Found"));

        String oldValue =
                convertToJson(notification);

        notification.setStatus("ARCHIVED");

        Notification updated =
                notificationRepository.save(notification);

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.logUpdate(
                "NOTIFICATION",
                String.valueOf(updated.getId()),
                performedBy,
                notification.getEmployee().getEmployeeId(),
                "Notification Archived Successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "ARCHIVE_NOTIFICATION",
                "NOTIFICATION",
                "Notification Archived Successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION",
                "NotificationService",
                "Notification Archived Successfully"
        );

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

        EmailNotification saved =
                emailNotificationRepository.save(emailNotification);

        String performedBy=getLoggedInEmployeeId();
        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate(
                "EMAIL_NOTIFICATION",
                performedBy,
                emailNotification.getEmployee().getEmployeeId(),
                employeeId,
                "Email Notification Created Successfully"
        );


        return "Email Notification Created Successfully";
    }


//=========================================================
// GET ALL EMAIL NOTIFICATIONS
//=========================================================

    public List<EmailNotification> getEmailNotifications() {

        return emailNotificationRepository.findAll();
    }


//=========================================================
// GET EMAIL NOTIFICATION BY ID
//=========================================================

    public EmailNotification getEmailNotification(Long id) {

        return emailNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Email Notification Not Found"));
    }


//=========================================================
// UPDATE EMAIL NOTIFICATION
//=========================================================

    public String updateEmailNotification(
            Long id,
            EmailNotification emailNotification) {

        EmailNotification existing =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email Notification Not Found"));


        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE DATA
        // =====================================================

        existing.setEmailAddress(
                emailNotification.getEmailAddress());

        existing.setSubject(
                emailNotification.getSubject());

        existing.setEmailStatus(
                emailNotification.getEmailStatus());

        existing.setSentAt(
                emailNotification.getSentAt());


        EmailNotification updated =
                emailNotificationRepository.save(existing);


        // =====================================================
        // AUDIT LOG
        // =====================================================
String performedBy=getLoggedInEmployeeId();
        auditLogsService.logCreate(
                "EMAIL_NOTIFICATION",
                performedBy,
                emailNotification.getEmployee().getEmployeeId(),
                emailNotification.getEmployee().getEmployeeId(),
                "Email Notification updated Successfully"
        );


        return "Email Notification Updated Successfully";
    }


//=========================================================
// DELETE EMAIL NOTIFICATION
//=========================================================

    public String deleteEmailNotification(Long id) {

        EmailNotification existing =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email Notification Not Found"));


        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // DELETE
        // =====================================================

        emailNotificationRepository.delete(existing);


        // =====================================================
        // AUDIT LOG
        // =====================================================
String performedBy=getLoggedInEmployeeId();
        auditLogsService.logDelete(
                "EMAIL_NOTIFICATION",
                oldData,
                performedBy,
               existing.getEmployee().getEmployeeId(),
                "Email Notification Deleted Successfully"

        );


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

        PushNotification saved =
                pushNotificationRepository.save(pushNotification);


        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PUSH_NOTIFICATION",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Push Notification created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_PUSH_NOTIFICATION",
                "PUSH_NOTIFICATION",
                "Push Notification created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PUSH_NOTIFICATION",
                "PushNotificationService",
                "Push Notification created successfully"
        );


        return "Push Notification Created Successfully";
    }


//=========================================================
// GET ALL PUSH NOTIFICATIONS
//=========================================================

    public List<PushNotification> getPushNotifications() {

        return pushNotificationRepository.findAll();
    }


//=========================================================
// GET PUSH NOTIFICATION BY ID
//=========================================================

    public PushNotification getPushNotification(Long id) {

        return pushNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Push Notification Not Found"));
    }


//=========================================================
// UPDATE PUSH NOTIFICATION
//=========================================================

    public String updatePushNotification(
            Long id,
            PushNotification pushNotification) {

        PushNotification existing =
                pushNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Push Notification Not Found"));


        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE DATA
        // =====================================================

        existing.setDeviceToken(
                pushNotification.getDeviceToken());

        existing.setPlatform(
                pushNotification.getPlatform());

        existing.setPushStatus(
                pushNotification.getPushStatus());

        existing.setSentAt(
                pushNotification.getSentAt());


        PushNotification updated =
                pushNotificationRepository.save(existing);


        // =====================================================
        // NEW DATA
        // =====================================================

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "PUSH_NOTIFICATION",
                String.valueOf(id),
                performedBy,
                null,
                "Push Notification updated successfully",
                oldData,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_PUSH_NOTIFICATION",
                "PUSH_NOTIFICATION",
                "Push Notification updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PUSH_NOTIFICATION",
                "PushNotificationService",
                "Push Notification updated successfully"
        );


        return "Push Notification Updated Successfully";
    }


//=========================================================
// DELETE PUSH NOTIFICATION
//=========================================================

    public String deletePushNotification(Long id) {

        PushNotification existing =
                pushNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Push Notification Not Found"));


        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        pushNotificationRepository.delete(existing);


        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.logDelete(
                "PUSH_NOTIFICATION",
                existing.getEmployee().getEmployeeId(),
                String.valueOf(id),
                performedBy,
                "Push Notification deleted successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_PUSH_NOTIFICATION",
                "PUSH_NOTIFICATION",
                "Push Notification deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PUSH_NOTIFICATION",
                "PushNotificationService",
                "Push Notification deleted successfully"
        );


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

        SmsNotification saved =
                smsNotificationRepository.save(smsNotification);


        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "SMS_NOTIFICATION",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "SMS Notification created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_SMS_NOTIFICATION",
                "SMS_NOTIFICATION",
                "SMS Notification created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "SMS_NOTIFICATION",
                "SmsNotificationService",
                "SMS Notification created successfully"
        );


        return "SMS Notification Created Successfully";
    }


//=========================================================
// GET ALL SMS NOTIFICATIONS
//=========================================================

    public List<SmsNotification> getSmsNotifications() {

        return smsNotificationRepository.findAll();
    }


//=========================================================
// GET SMS NOTIFICATION BY ID
//=========================================================

    public SmsNotification getSmsNotification(Long id) {

        return smsNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "SMS Notification Not Found"));
    }


//=========================================================
// UPDATE SMS NOTIFICATION
//=========================================================

    public String updateSmsNotification(
            Long id,
            SmsNotification smsNotification) {

        SmsNotification existing =
                smsNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SMS Notification Not Found"));


        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE DATA
        // =====================================================

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


        SmsNotification updated =
                smsNotificationRepository.save(existing);


        // =====================================================
        // NEW DATA
        // =====================================================

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "SMS_NOTIFICATION",
                String.valueOf(id),
                performedBy,
                null,
                "SMS Notification updated successfully",
                oldData,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_SMS_NOTIFICATION",
                "SMS_NOTIFICATION",
                "SMS Notification updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "SMS_NOTIFICATION",
                "SmsNotificationService",
                "SMS Notification updated successfully"
        );


        return "SMS Notification Updated Successfully";
    }


//=========================================================
// DELETE SMS NOTIFICATION
//=========================================================

    public String deleteSmsNotification(Long id) {

        SmsNotification existing =
                smsNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SMS Notification Not Found"));


        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        smsNotificationRepository.delete(existing);


        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.logDelete(
                "SMS_NOTIFICATION",
                String.valueOf(id),
                existing.getEmployee().getEmployeeId(),
                performedBy,
                "SMS Notification deleted successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_SMS_NOTIFICATION",
                "SMS_NOTIFICATION",
                "SMS Notification deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "SMS_NOTIFICATION",
                "SmsNotificationService",
                "SMS Notification deleted successfully"
        );


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
// CREATE NOTIFICATION TEMPLATE
//=========================================================

    public String createTemplate(
            NotificationTemplate template) {

        if (template.getStatus() == null) {
            template.setStatus("ACTIVE");
        }

        NotificationTemplate saved =
                notificationTemplateRepository.save(template);


        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "NOTIFICATION_TEMPLATE",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Notification Template created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_NOTIFICATION_TEMPLATE",
                "NOTIFICATION_TEMPLATE",
                "Notification Template created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_TEMPLATE",
                "NotificationTemplateService",
                "Notification Template created successfully"
        );


        return "Notification Template Created Successfully";
    }


//=========================================================
// GET ALL NOTIFICATION TEMPLATES
//=========================================================

    public List<NotificationTemplate> getTemplates() {

        return notificationTemplateRepository.findAll();
    }


//=========================================================
// GET NOTIFICATION TEMPLATE BY ID
//=========================================================

    public NotificationTemplate getTemplate(Long id) {

        return notificationTemplateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification Template Not Found"));
    }


//=========================================================
// UPDATE NOTIFICATION TEMPLATE
//=========================================================

    public String updateTemplate(
            Long id,
            NotificationTemplate template) {

        NotificationTemplate existing =
                notificationTemplateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Template Not Found"));


        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE DATA
        // =====================================================

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


        NotificationTemplate updated =
                notificationTemplateRepository.save(existing);


        // =====================================================
        // NEW DATA
        // =====================================================

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "NOTIFICATION_TEMPLATE",
                String.valueOf(id),
                performedBy,
                template.getId().toString(),
                "Notification Template updated successfully",
                oldData,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_NOTIFICATION_TEMPLATE",
                "NOTIFICATION_TEMPLATE",
                "Notification Template updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_TEMPLATE",
                "NotificationTemplateService",
                "Notification Template updated successfully"
        );


        return "Notification Template Updated Successfully";
    }


//=========================================================
// DELETE NOTIFICATION TEMPLATE
//=========================================================

    public String deleteTemplate(Long id) {

        NotificationTemplate existing =
                notificationTemplateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Template Not Found"));


        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        notificationTemplateRepository.delete(existing);


        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.logDelete(
                "NOTIFICATION_TEMPLATE",
                String.valueOf(id),
                performedBy,
                performedBy,
                "Notification Template deleted successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_NOTIFICATION_TEMPLATE",
                "NOTIFICATION_TEMPLATE",
                "Notification Template deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_TEMPLATE",
                "NotificationTemplateService",
                "Notification Template deleted successfully"
        );


        return "Notification Template Deleted Successfully";
    }



//=========================================================
// CREATE NOTIFICATION LOG
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

        NotificationLog saved =
                notificationLogRepository.save(notificationLog);


        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "NOTIFICATION_LOG",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Notification Log created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_NOTIFICATION_LOG",
                "NOTIFICATION_LOG",
                "Notification Log created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_LOG",
                "NotificationLogService",
                "Notification Log created successfully"
        );


        return "Notification Log Created Successfully";
    }


//=========================================================
// GET ALL NOTIFICATION LOGS
//=========================================================

    public List<NotificationLog> getNotificationLogs() {

        return notificationLogRepository.findAll();
    }


//=========================================================
// GET NOTIFICATION LOG BY ID
//=========================================================

    public NotificationLog getNotificationLog(Long id) {

        return notificationLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification Log Not Found"));
    }


//=========================================================
// GET EMPLOYEE NOTIFICATION LOGS
//=========================================================

    public List<NotificationLog> getEmployeeNotificationLogs(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Not Found"));

        return notificationLogRepository
                .findByEmployeeEmployeeId(employeeId);
    }


//=========================================================
// UPDATE NOTIFICATION LOG
//=========================================================

    public String updateNotificationLog(
            Long id,
            NotificationLog notificationLog) {

        NotificationLog existing =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Log Not Found"));


        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE DATA
        // =====================================================

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


        NotificationLog updated =
                notificationLogRepository.save(existing);


        // =====================================================
        // NEW DATA
        // =====================================================

        String newValue =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "NOTIFICATION_LOG",
                String.valueOf(id),
                performedBy,
                null,
                "Notification Log updated successfully",
                oldData,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_NOTIFICATION_LOG",
                "NOTIFICATION_LOG",
                "Notification Log updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_LOG",
                "NotificationLogService",
                "Notification Log updated successfully"
        );


        return "Notification Log Updated Successfully";
    }


//=========================================================
// DELETE NOTIFICATION LOG
//=========================================================

    public String deleteNotificationLog(Long id) {

        NotificationLog existing =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification Log Not Found"));


        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        notificationLogRepository.delete(existing);


        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.logDelete(
                "NOTIFICATION_LOG",
                String.valueOf(id),
                existing.getEmployee().getEmployeeId(),
                performedBy,
                "Notification Log deleted successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_NOTIFICATION_LOG",
                "NOTIFICATION_LOG",
                "Notification Log deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "NOTIFICATION_LOG",
                "NotificationLogService",
                "Notification Log deleted successfully"
        );


        return "Notification Log Deleted Successfully";
    }
}