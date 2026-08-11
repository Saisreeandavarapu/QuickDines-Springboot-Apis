package com.HRMS.QuickDines.Organization.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
//    @Query("""
//    SELECT h.employee
//    FROM Designation h
//    WHERE LOWER(h.designation.designationName)
//          = LOWER(:designationName)
//""")
//    List<Employee> findEmployeesByDesignationName(
//            @Param("designationName") String designationName);
}
