package com.HRMS.QuickDines.Organization.repo;

import com.HRMS.QuickDines.Organization.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
