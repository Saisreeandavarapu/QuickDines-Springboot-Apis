package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.Entity.EmployeePromotionStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.model.EmployeePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeePromotionRepository extends JpaRepository<EmployeePromotion, Long> {
    void deleteByEmployee(Employee employee);
    List<EmployeePromotion> findByEmployeeEmployeeId(String employeeId);

    List<EmployeePromotion> findByManagerEmployeeIdAndStatus(
            String employeeId,
            EmployeePromotionStatus status
    );

    List<EmployeePromotion> findByHrEmployeeIdAndStatus(
            String employeeId,
            EmployeePromotionStatus status
    );

    List<EmployeePromotion> findByAdminEmployeeIdAndStatus(
            String employeeId,
            EmployeePromotionStatus status
    );

    List<EmployeePromotion> findByStatus(
            EmployeePromotionStatus status
    );

}
