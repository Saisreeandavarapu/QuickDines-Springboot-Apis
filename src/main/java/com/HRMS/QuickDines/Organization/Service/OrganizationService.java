package com.HRMS.QuickDines.Organization.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Organization.DTO.DepartmentRequest;
import com.HRMS.QuickDines.Organization.DTO.DesignationRequest;
import com.HRMS.QuickDines.Organization.DTO.TeamRequest;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Organization.model.OrganizationHierarchy;
import com.HRMS.QuickDines.Organization.model.Team;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.HRMS.QuickDines.Organization.repo.DesignationRepository;
import com.HRMS.QuickDines.Organization.repo.OrganizationHierarchyRepository;
import com.HRMS.QuickDines.Organization.repo.TeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final TeamRepository teamRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


    // =====================================
    // Department Services
    // =====================================

    public String createDepartment(DepartmentRequest request) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company Not Found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch Not Found"));

        Department department = new Department();

        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentCode(request.getDepartmentCode());
        department.setDescription(request.getDescription());
        department.setStatus(request.getStatus());

        department.setCompany(company);
        department.setBranch(branch);

        departmentRepository.save(department);

//        String performedBy = getLoggedInEmployeeId();
//
//        auditLogsService.logCreate(
//                "ORGANIZATION",
//                String.valueOf(department.getId()),
//                performedBy,
//                department.getId().toString(),
//                "Department created successfully"
//        );
//
//        auditLogsService.logActivity(
//                performedBy,
//                "CREATE_DEPARTMENT",
//                "ORGANIZATION",
//                "Department created successfully",
//                ActivityStatus.SUCCESS,
//                getIpAddress(),
//                getBrowser(),
//                getOperatingSystem()
//        );
//
//        auditLogsService.logInfo(
//                "ORGANIZATION",
//                "DepartmentService",
//                "Department created successfully"
//        );

        return "Department Created Successfully";
    }

    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    public String updateDepartment(Long id, Department department) {

        Department existing = departmentRepository.findById(id).orElseThrow();

        existing.setDepartmentName(department.getDepartmentName());

        existing.setDepartmentCode(department.getDepartmentCode());



        existing.setDescription(department.getDescription());

        existing.setStatus(department.getStatus());

      Department updatedDepartmentType=  departmentRepository.save(existing);
        String oldValue =
                convertToJson(existing);
        String newValue =
                convertToJson(updatedDepartmentType);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "ORGANIZATION",
                String.valueOf(id),
                performedBy,
                department.getId().toString(),
                "Department  updated successfully",
                oldValue,
                newValue
        );
        auditLogsService.logActivity(
                performedBy,
                "CREATE_DEPARTMENT",
                "ORGANIZATION",
                "department updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "DepartmentService",
                "department  updated successfully"
        );

        return "Department Updated Successfully";
    }

    public String deleteDepartment(Long id) {

        Department existingDepartment = departmentRepository.findById(id) .orElseThrow(() -> new RuntimeException( "Department not found with id: " + id ));
        String deletedValue =
                convertToJson(existingDepartment);

        String performedBy =
                getLoggedInEmployeeId();

     //   leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog(
                "Department",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingDepartment.getId().toString(),
                "department Type deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_DEPARTMENT",
                "ORGANIZATION",
                "department deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "DepartmentService",
                "department deleted successfully"
        );

        return "Department Deleted Successfully";
    }


    // =====================================
    // Designation Services
    // =====================================

    public String createDesignation(DesignationRequest request) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new RuntimeException("Department Not Found"));

        // Validate department belongs to company
        if (!department.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException(
                    "Department does not belong to selected company"
            );
        }

        Designation designation = new Designation();

        designation.setDesignationName(request.getDesignationName());
        designation.setDesignationCode(request.getDesignationCode());
        designation.setLevel(request.getLevel());
        designation.setSalaryGrade(request.getSalaryGrade());

        designation.setCompany(company);
        designation.setDepartment(department);

        designationRepository.save(designation);


        //String performedBy = getLoggedInEmployeeId();

        //String newValue = convertToJson(leaveType);
//
//        auditLogsService.logCreate(
//                "ORGANIZATION",
//                String.valueOf(designation.getId()),
//                performedBy,
//                designation.getId().toString(),
//                "designation  created successfully"
//
//        );
//
//        auditLogsService.logActivity(
//                performedBy,
//                "CREATE_DESIGNATION",
//                "ORGANIZATION",
//                "designation created successfully",
//                ActivityStatus.SUCCESS,
//                getIpAddress(),
//                getBrowser(),
//                getOperatingSystem()
//        );
//
//        auditLogsService.logInfo(
//                "ORGANIZATION",
//                "DESIGNATIONService",
//                "designation  created successfully"
//        );

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

       Designation updatedDesignation= designationRepository.save(existing);
        String oldValue =
                convertToJson(existing);
        String newValue =
                convertToJson(updatedDesignation);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "ORGANIZATION",
                String.valueOf(id),
                performedBy,
                designation.getId().toString(),
                "designation  updated successfully",
                oldValue,
                newValue
        );
        auditLogsService.logActivity(
                performedBy,
                "CREATE_DEPARTMENT",
                "ORGANIZATION",
                "designation updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "DepartmentService",
                "designation  updated successfully"
        );

        return "Designation Updated Successfully";
    }

    public String deleteDesignation(Long id) {

        Designation existingDesignation = designationRepository.findById(id) .orElseThrow(() -> new RuntimeException( "Designation not found with id: " + id));
        String deletedValue =
                convertToJson(existingDesignation);

        String performedBy =
                getLoggedInEmployeeId();

        //   leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog(
                "Designation",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingDesignation.getId().toString(),
                "designation Type deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_DESIGNATION",
                "ORGANIZATION",
                "designation deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "DesignationService",
                "designation deleted successfully"
        );

        return "Designation Deleted Successfully";
    }


    // =====================================
    // Team Services
    // =====================================

    public String createTeam(TeamRequest request) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new RuntimeException("Department Not Found"));

        // Validate branch belongs to company
        if (!branch.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException(
                    "Branch does not belong to selected company"
            );
        }

        // Validate department belongs to company
        if (!department.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException(
                    "Department does not belong to selected company"
            );
        }

        Team team = new Team();

        team.setTeamName(request.getTeamName());
        team.setTeamLead(request.getTeamLead());
        team.setNumberOfMembers(request.getNumberOfMembers());
        team.setDescription(request.getDescription());
        team.setStatus(request.getStatus());

        team.setCompany(company);
        team.setBranch(branch);
        team.setDepartment(department);

        teamRepository.save(team);


   //     String performedBy = getLoggedInEmployeeId();

        //String newValue = convertToJson(leaveType);

//        auditLogsService.logCreate(
//                "ORGANIZATION",
//                String.valueOf(team.getId()),
//                performedBy,
//                team.getId().toString(),
//                "team  created successfully"
//
//        );
//
//        auditLogsService.logActivity(
//                performedBy,
//                "CREATE_TEAM",
//                "ORGANIZATION",
//                "team created successfully",
//                ActivityStatus.SUCCESS,
//                getIpAddress(),
//                getBrowser(),
//                getOperatingSystem()
//        );
//
//        auditLogsService.logInfo(
//                "ORGANIZATION",
//                "TeamService",
//                "team  created successfully"
//        );

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

        Team updatedTeam= teamRepository.save(existing);
        String oldValue =
                convertToJson(existing);
        String newValue =
                convertToJson(updatedTeam);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "ORGANIZATION",
                String.valueOf(id),
                performedBy,
                team.getId().toString(),
                "Team  updated successfully",
                oldValue,
                newValue
        );
        auditLogsService.logActivity(
                performedBy,
                "CREATE_DEPARTMENT",
                "ORGANIZATION",
                "Team updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "TeamService",
                "Team  updated successfully"
        );

        return "Team Updated Successfully";
    }

    public String deleteTeam(Long id) {

        Team existingTeam = teamRepository.findById(id) .orElseThrow(() -> new RuntimeException( "Team not found with id: " + id));
        String deletedValue =
                convertToJson(existingTeam);

        String performedBy =
                getLoggedInEmployeeId();

        //   leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog(
                "Team",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingTeam.getId().toString(),
                "Team Type deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TEAM",
                "ORGANIZATION",
                "Team deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "TeamService",
                "Team deleted successfully"
        );

        return "Team Deleted Successfully";
    }


    // =====================================
    // Organization Hierarchy Services
    // =====================================

    public String createHierarchy(OrganizationHierarchy hierarchy) {

        hierarchyRepository.save(hierarchy);
        String performedBy = getLoggedInEmployeeId();

        //String newValue = convertToJson(leaveType);

        auditLogsService.logCreate(
                "ORGANIZATION",
                String.valueOf(hierarchy.getId()),
                performedBy,
                hierarchy.getId().toString(),
                "hierarchy  created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_HIERARCHY",
                "ORGANIZATION",
                "hierarchy created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "HierarchyService",
                "hierarchy  created successfully"
        );

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

       OrganizationHierarchy updatedHieracrchy= hierarchyRepository.save(existing);
        String oldValue =
                convertToJson(existing);
        String newValue =
                convertToJson(updatedHieracrchy);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "ORGANIZATION",
                String.valueOf(id),
                performedBy,
                hierarchy.getId().toString(),
                "hierarchy  updated successfully",
                oldValue,
                newValue
        );
        auditLogsService.logActivity(
                performedBy,
                "CREATE_HIERARCHY",
                "ORGANIZATION",
                "hierarchy updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "HierarchyService",
                "hierarchy  updated successfully"
        );

        return "Hierarchy Updated Successfully";
    }

    public String deleteHierarchy(Long id) {

        OrganizationHierarchy hierarchy = hierarchyRepository.findById(id) .orElseThrow(() -> new RuntimeException( "Organization hierarchy not found with id: " + id));
        String deletedValue =
                convertToJson(hierarchy);

        String performedBy =
                getLoggedInEmployeeId();

        //   leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog(
                "OrganizationHierarchy",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                hierarchy.getId().toString(),
                "OrganizationHierarchy Type deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_ORGANIZATION_HIERARCHY",
                "ORGANIZATION",
                "OrganizationHierarchy deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ORGANIZATION",
                "OrganizationHierarchyService",
                "OrganizationHierarchy deleted successfully"
        );

        return "Hierarchy Deleted Successfully";
    }


}
