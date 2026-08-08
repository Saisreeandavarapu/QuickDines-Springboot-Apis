package com.HRMS.QuickDines.Company.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.BranchLocation;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchLocationRepository;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final BranchLocationRepository branchLocationRepository;

    // AUDIT LOG SERVICES
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper;

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
    String performedBy = getLoggedInEmployeeId();


    //=========================================================
    // COMPANY MANAGEMENT
    //=========================================================

    public String createCompany(Company company) {

        companyRepository.save(company);

        // ACTIVITY LOG
        auditLogsService.logActivity(
                performedBy,
                "CREATE_COMPANY",
                "COMPANY",
                "Company created successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // AUDIT LOG
        auditLogsService.createAuditLog(
                "COMPANY",
                String.valueOf(company.getId()),
                AuditActionType.CREATE,
                null,
                null,
                "Company created successfully",
                null,
                company.toString(),
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "COMPANY",
                "CompanyService",
                "Company created successfully. Company ID: "
                        + company.getId()
        );

        return "Company Created Successfully";
    }


    public List<Company> getAllCompanies() {

        List<Company> companies = companyRepository.findAll();

        // ACTIVITY LOG
        auditLogsService.logActivity(
                null,
                "GET_ALL_COMPANIES",
                "COMPANY",
                "All companies retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "COMPANY",
                "CompanyService",
                "All companies retrieved successfully"
        );

        return companies;
    }


    public Company getCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        // ACTIVITY LOG
        auditLogsService.logActivity(
                performedBy,
                "GET_COMPANY",
                "COMPANY",
                "Company retrieved successfully. Company ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "COMPANY",
                "CompanyService",
                "Company retrieved successfully. Company ID: " + id
        );

        return company;
    }


    public String updateCompany(Long id, Company company) {

        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        String oldValue = existingCompany.toString();

        existingCompany.setCompanyCode(company.getCompanyCode());
        existingCompany.setCompanyName(company.getCompanyName());
        existingCompany.setLegalName(company.getLegalName());
        existingCompany.setGstNumber(company.getGstNumber());
        existingCompany.setPanNumber(company.getPanNumber());
        existingCompany.setCinNumber(company.getCinNumber());
        existingCompany.setEmail(company.getEmail());
        existingCompany.setPhone(company.getPhone());
        existingCompany.setWebsite(company.getWebsite());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setCity(company.getCity());
        existingCompany.setState(company.getState());
        existingCompany.setCountry(company.getCountry());
        existingCompany.setPostalCode(company.getPostalCode());
        existingCompany.setLogoUrl(company.getLogoUrl());
        existingCompany.setStatus(company.getStatus());

        companyRepository.save(existingCompany);

        
        String newValue=existingCompany.toString();


        // ACTIVITY LOG
        auditLogsService.logActivity(
                performedBy,
                "UPDATE_COMPANY",
                "COMPANY",
                "Company updated successfully. Company ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // AUDIT LOG
        auditLogsService.createAuditLog(
                "COMPANY",
                existingCompany.getCompanyCode(),
                AuditActionType.UPDATE,
                performedBy,
                null,
                "Company updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "COMPANY",
                "CompanyService",
                "Company updated successfully. Company ID: " + id
        );

        return "Company Updated Successfully";
    }


    public String deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        companyRepository.delete(company);

        // ACTIVITY LOG
        auditLogsService.logActivity(
                performedBy,
                "DELETE_COMPANY",
                "COMPANY",
                "Company deleted successfully. Company ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // AUDIT LOG
        auditLogsService.createAuditLog(
                "COMPANY",
                company.getCompanyCode(),
                AuditActionType.DELETE,
                performedBy,
                performedBy,
                "Company deleted successfully",
                company.toString(),
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "COMPANY",
                "CompanyService",
                "Company deleted successfully. Company ID: " + id
        );

        return "Company Deleted Successfully";
    }


    //=========================================================
    // BRANCH MANAGEMENT
    //=========================================================

    public String createBranch(Long companyId, Branch branch) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        branch.setCompany(company);

        branchRepository.save(branch);

        // ACTIVITY LOG
        auditLogsService.logActivity(
                null,
                "CREATE_BRANCH",
                "BRANCH",
                "Branch created successfully. Branch ID: " + branch.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // AUDIT LOG
        auditLogsService.createAuditLog(
                "BRANCH",
                branch.getBranchCode(),
                AuditActionType.CREATE,
                performedBy,
                performedBy,
                "Branch created successfully",
                null,
                branch.toString(),
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // SYSTEM LOG
        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "Branch created successfully. Branch ID: "
                        + branch.getId()
        );

        return "Branch Created Successfully";
    }


    public List<Branch> getAllBranches() {

        List<Branch> branches = branchRepository.findAll();

        auditLogsService.logActivity(
                null,
                "GET_ALL_BRANCHES",
                "BRANCH",
                "All branches retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "All branches retrieved successfully"
        );

        return branches;
    }


    public Branch getBranch(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        auditLogsService.logActivity(
                null,
                "GET_BRANCH",
                "BRANCH",
                "Branch retrieved successfully. Branch ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "Branch retrieved successfully. Branch ID: " + id
        );

        return branch;
    }


    public List<Branch> getBranchesByCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        List<Branch> branches =
                branchRepository.findByCompany(company);

        auditLogsService.logActivity(
                null,
                "GET_COMPANY_BRANCHES",
                "BRANCH",
                "Branches retrieved for Company ID: " + companyId,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "Branches retrieved for Company ID: " + companyId
        );

        return branches;
    }


    public String updateBranch(Long id, Branch branch) {

        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        String oldValue = existingBranch.toString();

        existingBranch.setBranchCode(branch.getBranchCode());
        existingBranch.setBranchName(branch.getBranchName());
        existingBranch.setBranchType(branch.getBranchType());
        existingBranch.setManager(branch.getManager());
        existingBranch.setEmail(branch.getEmail());
        existingBranch.setPhone(branch.getPhone());
        existingBranch.setAddress(branch.getAddress());
        existingBranch.setCity(branch.getCity());
        existingBranch.setState(branch.getState());
        existingBranch.setCountry(branch.getCountry());
        existingBranch.setPostalCode(branch.getPostalCode());
        existingBranch.setStatus(branch.getStatus());

        branchRepository.save(existingBranch);

        String newValue = existingBranch.toString();

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_BRANCH",
                "BRANCH",
                "Branch updated successfully. Branch ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "BRANCH",
                existingBranch.getBranchCode(),
                AuditActionType.UPDATE,
                performedBy,
                null,
                "Branch updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "Branch updated successfully. Branch ID: " + id
        );

        return "Branch Updated Successfully";
    }


    public String deleteBranch(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        branchRepository.delete(branch);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_BRANCH",
                "BRANCH",
                "Branch deleted successfully. Branch ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "BRANCH",
                branch.getBranchCode(),
                AuditActionType.DELETE,
                null,
                null,
                "Branch deleted successfully",
                branch.toString(),
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH",
                "CompanyService",
                "Branch deleted successfully. Branch ID: " + id
        );

        return "Branch Deleted Successfully";
    }


    //=========================================================
    // BRANCH LOCATION MANAGEMENT
    //=========================================================

    public String createBranchLocation(
            Long branchId,
            BranchLocation location) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        location.setBranch(branch);

        branchLocationRepository.save(location);

        auditLogsService.logActivity(
                null,
                "CREATE_BRANCH_LOCATION",
                "BRANCH_LOCATION",
                "Branch location created successfully. Location ID: "
                        + location.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "BRANCH_LOCATION",
                String.valueOf(location.getId()),
                AuditActionType.CREATE,
                performedBy,
                null,
                "Branch location created successfully",
                null,
                location.toString(),
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "Branch location created successfully. Location ID: "
                        + location.getId()
        );

        return "Branch Location Created Successfully";
    }


    public List<BranchLocation> getAllBranchLocations() {

        List<BranchLocation> locations =
                branchLocationRepository.findAll();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_BRANCH_LOCATIONS",
                "BRANCH_LOCATION",
                "All branch locations retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "All branch locations retrieved successfully"
        );

        return locations;
    }


    public BranchLocation getBranchLocation(Long id) {

        BranchLocation location =
                branchLocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Branch Location Not Found"));

        auditLogsService.logActivity(
                performedBy,
                "GET_BRANCH_LOCATION",
                "BRANCH_LOCATION",
                "Branch location retrieved successfully. Location ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "Branch location retrieved successfully. Location ID: " + id
        );

        return location;
    }


    public List<BranchLocation> getLocationsByBranch(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        List<BranchLocation> locations =
                branchLocationRepository.findByBranch(branch);

        auditLogsService.logActivity(
                performedBy,
                "GET_BRANCH_LOCATIONS",
                "BRANCH_LOCATION",
                "Locations retrieved for Branch ID: " + branchId,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "Locations retrieved for Branch ID: " + branchId
        );

        return locations;
    }


    public String updateBranchLocation(
            Long id,
            BranchLocation location) {

        BranchLocation existingLocation =
                branchLocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Branch Location Not Found"));

        String oldValue = existingLocation.toString();

        existingLocation.setBuildingName(location.getBuildingName());
        existingLocation.setFloor(location.getFloor());
        existingLocation.setAddressLine1(location.getAddressLine1());
        existingLocation.setAddressLine2(location.getAddressLine2());
        existingLocation.setCity(location.getCity());
        existingLocation.setState(location.getState());
        existingLocation.setCountry(location.getCountry());
        existingLocation.setPostalCode(location.getPostalCode());
        existingLocation.setLatitude(location.getLatitude());
        existingLocation.setLongitude(location.getLongitude());
        existingLocation.setGeoRadius(location.getGeoRadius());

        branchLocationRepository.save(existingLocation);

        String newValue = existingLocation.toString();

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_BRANCH_LOCATION",
                "BRANCH_LOCATION",
                "Branch location updated successfully. Location ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "BRANCH_LOCATION",
                String.valueOf(id),
                AuditActionType.UPDATE,
                performedBy,
                null,
                "Branch location updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "Branch location updated successfully. Location ID: " + id
        );

        return "Branch Location Updated Successfully";
    }


    public String deleteBranchLocation(Long id) {

        BranchLocation location =
                branchLocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Branch Location Not Found"));

        branchLocationRepository.delete(location);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_BRANCH_LOCATION",
                "BRANCH_LOCATION",
                "Branch location deleted successfully. Location ID: " + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "BRANCH_LOCATION",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                null,
                "Branch location deleted successfully",
                location.toString(),
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "BRANCH_LOCATION",
                "CompanyService",
                "Branch location deleted successfully. Location ID: " + id
        );

        return "Branch Location Deleted Successfully";
    }
}
