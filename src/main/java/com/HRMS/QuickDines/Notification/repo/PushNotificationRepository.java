package com.HRMS.QuickDines.Notification.repo;

import com.HRMS.QuickDines.Notification.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
    List<PushNotification> findBySentStatus(String sentStatus);

}
