package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
}
