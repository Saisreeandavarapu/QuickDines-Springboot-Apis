package com.HRMS.QuickDines.CRM.repo;

import com.HRMS.QuickDines.CRM.model.CustomerMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMeetingRepository extends JpaRepository<CustomerMeeting,Long> {
}
