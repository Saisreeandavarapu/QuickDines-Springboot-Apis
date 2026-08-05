package com.HRMS.QuickDines.Training.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Training.model.TrainingCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingCertificateRepository extends JpaRepository<TrainingCertificate,Long> {
    List<TrainingCertificate> findByEmployee(Employee employee);
}
