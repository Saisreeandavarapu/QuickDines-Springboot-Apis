package com.HRMS.QuickDines.Organization.Service;

import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Organization.model.OrganizationHierarchy;
import com.HRMS.QuickDines.Organization.model.Team;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.HRMS.QuickDines.Organization.repo.DesignationRepository;
import com.HRMS.QuickDines.Organization.repo.OrganizationHierarchyRepository;
import com.HRMS.QuickDines.Organization.repo.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final TeamRepository teamRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;


    // =====================================
    // Department Services
    // =====================================

    public String createDepartment(Department department) {
        departmentRepository.save(department);
        return "Department Created Successfully";
    }

    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    public String updateDepartment(Long id, Department department) {

        Department existing = departmentRepository.findById(id).orElseThrow();

        existing.setDepartmentName(department.getDepartmentName());

        existing.setDepartmentCode(department.getDepartmentCode());

        existing.setManagerName(department.getManagerName());

        existing.setDescription(department.getDescription());

        existing.setStatus(department.getStatus());

        departmentRepository.save(existing);

        return "Department Updated Successfully";
    }

    public String deleteDepartment(Long id) {

        departmentRepository.deleteById(id);

        return "Department Deleted Successfully";
    }


    // =====================================
    // Designation Services
    // =====================================

    public String createDesignation(Designation designation) {

        designationRepository.save(designation);

        return "Designation Created Successfully";
    }

    public List<Designation> getAllDesignation() {

        return designationRepository.findAll();
    }

    public String updateDesignation(Long id, Designation designation) {

        Designation existing = designationRepository.findById(id).orElseThrow();

        existing.setDesignationName(designation.getDesignationName());

        existing.setDesignationCode(designation.getDesignationCode());

        existing.setLevel(designation.getLevel());

        existing.setSalaryGrade(designation.getSalaryGrade());

        designationRepository.save(existing);

        return "Designation Updated Successfully";
    }

    public String deleteDesignation(Long id) {

        designationRepository.deleteById(id);

        return "Designation Deleted Successfully";
    }


    // =====================================
    // Team Services
    // =====================================

    public String createTeam(Team team) {

        teamRepository.save(team);

        return "Team Created Successfully";
    }

    public List<Team> getAllTeams() {

        return teamRepository.findAll();
    }

    public String updateTeam(Long id, Team team) {

        Team existing = teamRepository.findById(id).orElseThrow();

        existing.setTeamName(team.getTeamName());

        existing.setTeamLead(team.getTeamLead());

        existing.setNumberOfMembers(team.getNumberOfMembers());

        existing.setDescription(team.getDescription());

        existing.setStatus(team.getStatus());

        teamRepository.save(existing);

        return "Team Updated Successfully";
    }

    public String deleteTeam(Long id) {

        teamRepository.deleteById(id);

        return "Team Deleted Successfully";
    }


    // =====================================
    // Organization Hierarchy Services
    // =====================================

    public String createHierarchy(OrganizationHierarchy hierarchy) {

        hierarchyRepository.save(hierarchy);

        return "Hierarchy Created Successfully";
    }

    public List<OrganizationHierarchy> getAllHierarchies() {

        return hierarchyRepository.findAll();
    }

    public String updateHierarchy(Long id, OrganizationHierarchy hierarchy) {

        OrganizationHierarchy existing = hierarchyRepository.findById(id).orElseThrow();

        existing.setReportingManager(hierarchy.getReportingManager());

        existing.setDepartmentHead(hierarchy.getDepartmentHead());

        existing.setHierarchyLevel(hierarchy.getHierarchyLevel());

        hierarchyRepository.save(existing);

        return "Hierarchy Updated Successfully";
    }

    public String deleteHierarchy(Long id) {

        hierarchyRepository.deleteById(id);

        return "Hierarchy Deleted Successfully";
    }

}
