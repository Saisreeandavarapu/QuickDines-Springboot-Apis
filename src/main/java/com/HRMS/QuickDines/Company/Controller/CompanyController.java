package com.HRMS.QuickDines.Company.Controller;

import com.HRMS.QuickDines.Company.Service.CompanyService;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.BranchLocation;
import com.HRMS.QuickDines.Company.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    // =========================================================
    // COMPANY MANAGEMENT
    // =========================================================

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('COMPANY_CREATE')")
    public ResponseEntity<?> createCompany(
            @RequestBody Company company) {

        return ResponseEntity.ok(
                service.createCompany(company));
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    public ResponseEntity<?> getAllCompanies() {

        return ResponseEntity.ok(
                service.getAllCompanies());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    public ResponseEntity<?> getCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCompany(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company) {

        return ResponseEntity.ok(
                service.updateCompany(id, company));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_DELETE')")
    public ResponseEntity<?> deleteCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCompany(id));
    }


    // =========================================================
    // BRANCH MANAGEMENT
    // =========================================================

    @PostMapping("/branch/{companyId}")
   @PreAuthorize("hasAuthority('BRANCH_CREATE')")
    public ResponseEntity<?> createBranch(
            @PathVariable Long companyId,
            @RequestBody Branch branch) {

        return ResponseEntity.ok(
                service.createBranch(companyId, branch));
    }


    @GetMapping("/branches")
   @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<?> getAllBranches() {

        return ResponseEntity.ok(
                service.getAllBranches());
    }


    @GetMapping("/branch/{id}")
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<?> getBranch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBranch(id));
    }


    @GetMapping("/company/{companyId}/branches")
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<?> getBranchesByCompany(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                service.getBranchesByCompany(companyId));
    }


    @PutMapping("/branch/{id}")
    @PreAuthorize("hasAuthority('BRANCH_UPDATE')")
    public ResponseEntity<?> updateBranch(
            @PathVariable Long id,
            @RequestBody Branch branch) {

        return ResponseEntity.ok(
                service.updateBranch(id, branch));
    }


    @DeleteMapping("/branch/{id}")
    @PreAuthorize("hasAuthority('BRANCH_DELETE')")
    public ResponseEntity<?> deleteBranch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBranch(id));
    }


    // =========================================================
    // BRANCH LOCATION MANAGEMENT
    // =========================================================

    @PostMapping("/location/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_CREATE')")
    public ResponseEntity<?> createBranchLocation(
            @PathVariable Long branchId,
            @RequestBody BranchLocation location) {

        return ResponseEntity.ok(
                service.createBranchLocation(branchId, location));
    }


    @GetMapping("/locations")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_VIEW')")
    public ResponseEntity<?> getAllBranchLocations() {

        return ResponseEntity.ok(
                service.getAllBranchLocations());
    }


    @GetMapping("/location/{id}")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_VIEW')")
    public ResponseEntity<?> getBranchLocation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBranchLocation(id));
    }


    @GetMapping("/branch/{branchId}/locations")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_VIEW')")
    public ResponseEntity<?> getLocationsByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                service.getLocationsByBranch(branchId));
    }


    @PutMapping("/location/{id}")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_UPDATE')")
    public ResponseEntity<?> updateBranchLocation(
            @PathVariable Long id,
            @RequestBody BranchLocation location) {

        return ResponseEntity.ok(
                service.updateBranchLocation(id, location));
    }


    @DeleteMapping("/location/{id}")
    @PreAuthorize("hasAuthority('BRANCH_LOCATION_DELETE')")
    public ResponseEntity<?> deleteBranchLocation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBranchLocation(id));
    }
}