package com.HRMS.QuickDines.Notification.repo;

import com.HRMS.QuickDines.Notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByNotificationStatus(String status);

    Long countByNotificationStatus(String status);

}
