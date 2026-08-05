package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
}
