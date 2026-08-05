package com.HRMS.QuickDines.Notification.repo;

import com.HRMS.QuickDines.Notification.model.SmsNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsNotificationRepository extends JpaRepository<SmsNotification, Long> {
}
