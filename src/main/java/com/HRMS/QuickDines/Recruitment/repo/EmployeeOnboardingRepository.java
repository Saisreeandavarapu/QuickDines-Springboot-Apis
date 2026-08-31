package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Recruitment.Entity.OnboardingStatus;
import com.HRMS.QuickDines.Recruitment.model.EmployeeOnboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeOnboardingRepository
        extends JpaRepository<EmployeeOnboarding, Long> {

    List<EmployeeOnboarding> findByManager(Employee manager);

    List<EmployeeOnboarding> findByEmployee(Employee employee);

    List<EmployeeOnboarding> findByStatus(
            OnboardingStatus status);

    List<EmployeeOnboarding> findByManagerAndStatus(
            Employee manager,
            OnboardingStatus status);
}