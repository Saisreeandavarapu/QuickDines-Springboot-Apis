package com.HRMS.QuickDines.Notification.repo;

import com.HRMS.QuickDines.Notification.model.WhatsappNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhatsappNotificationRepository extends JpaRepository<WhatsappNotification, Long> {
}
