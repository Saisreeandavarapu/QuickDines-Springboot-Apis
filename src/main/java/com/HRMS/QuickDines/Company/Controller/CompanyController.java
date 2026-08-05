package com.HRMS.QuickDines.Company.Controller;

import com.HRMS.QuickDines.Company.Service.CompanyService;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.BranchLocation;
import com.HRMS.QuickDines.Company.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;


    //=========================================================
    // COMPANY MANAGEMENT
    //=========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(
            @RequestBody Company company) {

        return ResponseEntity.ok(
                service.createCompany(company));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies() {

        return ResponseEntity.ok(
                service.getAllCompanies());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCompany(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company) {

        return ResponseEntity.ok(
                service.updateCompany(id, company));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCompany(id));
    }


    //=========================================================
    // BRANCH MANAGEMENT
    //=========================================================

    @PostMapping("/branch/{companyId}")
    public ResponseEntity<?> createBranch(
            @PathVariable Long companyId,
            @RequestBody Branch branch) {

        return ResponseEntity.ok(
                service.createBranch(companyId, branch));
    }


    @GetMapping("/branches")
    public ResponseEntity<?> getAllBranches() {

        return ResponseEntity.ok(
                service.getAllBranches());
    }


    @GetMapping("/branch/{id}")
    public ResponseEntity<?> getBranch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBranch(id));
    }


    @GetMapping("/company/{companyId}/branches")
    public ResponseEntity<?> getBranchesByCompany(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                service.getBranchesByCompany(companyId));
    }


    @PutMapping("/branch/{id}")
    public ResponseEntity<?> updateBranch(
            @PathVariable Long id,
            @RequestBody Branch branch) {

        return ResponseEntity.ok(
                service.updateBranch(id, branch));
    }


    @DeleteMapping("/branch/{id}")
    public ResponseEntity<?> deleteBranch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBranch(id));
    }


    //=========================================================
    // BRANCH LOCATION MANAGEMENT
    //=========================================================

    @PostMapping("/location/{branchId}")
    public ResponseEntity<?> createBranchLocation(
            @PathVariable Long branchId,
            @RequestBody BranchLocation location) {

        return ResponseEntity.ok(
                service.createBranchLocation(branchId, location));
    }


    @GetMapping("/locations")
    public ResponseEntity<?> getAllBranchLocations() {

        return ResponseEntity.ok(
                service.getAllBranchLocations());
    }


    @GetMapping("/location/{id}")
    public ResponseEntity<?> getBranchLocation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBranchLocation(id));
    }


    @GetMapping("/branch/{branchId}/locations")
    public ResponseEntity<?> getLocationsByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                service.getLocationsByBranch(branchId));
    }


    @PutMapping("/location/{id}")
    public ResponseEntity<?> updateBranchLocation(
            @PathVariable Long id,
            @RequestBody BranchLocation location) {

        return ResponseEntity.ok(
                service.updateBranchLocation(id, location));
    }


    @DeleteMapping("/location/{id}")
    public ResponseEntity<?> deleteBranchLocation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBranchLocation(id));
    }
}