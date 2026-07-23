package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.model.Attendance;
import com.HRMS.QuickDines.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    List<Attendance> findByEmployeeId(String employeeId);


    Optional<Attendance>
    findTopByEmployeeIdOrderByIdDesc(String employeeId);


    Optional<Attendance>
    findByEmployeeIdAndCreatedAtBetween(
            String employeeId,
            LocalDateTime start,
            LocalDateTime end);

    boolean existsByEmployeeAndCreatedAtBetween(Employee employee, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<Attendance> findByEmployee(Employee employee);
}
