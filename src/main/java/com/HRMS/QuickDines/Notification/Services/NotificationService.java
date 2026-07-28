package com.HRMS.QuickDines.Notification.Services;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Notification.model.EmailNotification;
import com.HRMS.QuickDines.Notification.model.Notification;
import com.HRMS.QuickDines.Notification.model.PushNotification;
import com.HRMS.QuickDines.Notification.repo.EmailNotificationRepository;
import com.HRMS.QuickDines.Notification.repo.NotificationRepository;
import com.HRMS.QuickDines.Notification.repo.PushNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final PushNotificationRepository pushNotificationRepository;


    //=================================
    // NOTIFICATIONS
    //=================================

    public String createNotification(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Notification notification = new Notification();

        notification.setEmployee(employee);
        notification.setTitle("Welcome");
        notification.setMessage("Welcome to QuickDines");
        notification.setNotificationType("SYSTEM");
        notification.setNotificationStatus("UNREAD");

        notificationRepository.save(notification);

        return "Notification Created Successfully";
    }


    public Object getNotifications() {

        return notificationRepository.findAll();
    }


    public Object getNotification(Long id) {

        return notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification Not Found"));
    }


    public String updateNotification(Long id) {

        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Notification Not Found"));

        notification.setNotificationStatus("READ");

        notificationRepository.save(notification);

        return "Notification Updated Successfully";
    }


    public String deleteNotification(Long id) {

        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Notification Not Found"));

        notificationRepository.delete(notification);

        return "Notification Deleted Successfully";
    }



    //=================================
    // EMAIL NOTIFICATIONS
    //=================================

    public String createEmailNotification(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmailNotification email = new EmailNotification();

        email.setEmployee(employee);
        email.setEmailSubject("Welcome Mail");
        email.setEmailMessage("Welcome to QuickDines HRMS.");
        email.setSentStatus("SENT");
        email.setSentDate(LocalDateTime.from(LocalDate.now()));

        emailNotificationRepository.save(email);

        return "Email Notification Created Successfully";
    }


    public List<EmailNotification> getEmailNotifications() {

        return emailNotificationRepository.findAll();

    }


    public List<EmailNotification> getEmailNotification(Long id) {

        return Collections.singletonList(emailNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Email Notification Not Found")));

    }


    public String updateEmailNotification(Long id) {

        EmailNotification email = emailNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Email Notification Not Found"));

        email.setSentStatus("SENT");

        emailNotificationRepository.save(email);

        return "Email Notification Updated Successfully";
    }


    public String deleteEmailNotification(Long id) {

        EmailNotification email = emailNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Email Notification Not Found"));

        emailNotificationRepository.delete(email);

        return "Email Notification Deleted Successfully";
    }


//=================================
// PUSH NOTIFICATIONS
//=================================

    public String createPushNotification(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        PushNotification pushNotification = new PushNotification();

        pushNotification.setEmployee(employee);
        pushNotification.setNotificationTitle("Welcome Notification");
        pushNotification.setNotificationMessage("Welcome to QuickDines HRMS.");
        pushNotification.setSentStatus("SENT");
        pushNotification.setSentDate(LocalDateTime.from(LocalDate.now()));

        pushNotificationRepository.save(pushNotification);

        return "Push Notification Created Successfully";
    }


    public Object getPushNotifications() {

        return pushNotificationRepository.findAll();

    }


    public Object getPushNotification(Long id) {

        return pushNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Push Notification Not Found"));

    }


    public String updatePushNotification(Long id) {

        PushNotification pushNotification = pushNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Push Notification Not Found"));

        pushNotification.setSentStatus("SENT");

        pushNotificationRepository.save(pushNotification);

        return "Push Notification Updated Successfully";
    }


    public String deletePushNotification(Long id) {

        PushNotification pushNotification = pushNotificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Push Notification Not Found"));

        pushNotificationRepository.delete(pushNotification);

        return "Push Notification Deleted Successfully";
    }


//=================================
// REPORTS
//=================================

    public Object readNotifications() {

        return notificationRepository.findByNotificationStatus("READ");
    }


    public Object unreadNotifications() {

        return notificationRepository.findByNotificationStatus("UNREAD");
    }

    public Object sentEmails() {

        return emailNotificationRepository
                .findBySentStatus("SENT");

    }


    public Object pendingEmails() {

        return emailNotificationRepository
                .findBySentStatus("PENDING");

    }

    public Object sentPushNotifications() {

        return pushNotificationRepository
                .findBySentStatus("SENT");

    }


    public Object pendingPushNotifications() {

        return pushNotificationRepository
                .findBySentStatus("PENDING");

    }


//=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts() {

        Map<String, Long> counts = new HashMap<>();

        counts.put("Total Notifications",
                notificationRepository.count());

        counts.put("Read Notifications",
                notificationRepository.countByNotificationStatus("READ"));

        counts.put("Unread Notifications",
                notificationRepository.countByNotificationStatus("UNREAD"));

        return counts;
    }
}
