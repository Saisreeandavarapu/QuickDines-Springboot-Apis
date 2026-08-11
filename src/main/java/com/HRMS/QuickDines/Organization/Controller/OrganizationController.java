package com.HRMS.QuickDines.Organization.Controller;

import com.HRMS.QuickDines.Organization.Service.OrganizationService;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Organization.model.OrganizationHierarchy;
import com.HRMS.QuickDines.Organization.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;


    // ==============================
    // Department APIs
    // ==============================

    @PostMapping("/department")
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    public ResponseEntity<?> createDepartment(
            @RequestBody Department department) {

        return ResponseEntity.ok(
                service.createDepartment(department));
    }


    @GetMapping("/departments")
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    public ResponseEntity<?> getAllDepartments() {

        return ResponseEntity.ok(
                service.getAllDepartments());
    }


    @PutMapping("/department/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {

        return ResponseEntity.ok(
                service.updateDepartment(id, department));
    }


    @DeleteMapping("/department/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDepartment(id));
    }


    // ==============================
    // Designation APIs
    // ==============================

    @PostMapping("/designation")
    @PreAuthorize("hasAuthority('DESIGNATION_CREATE')")
    public ResponseEntity<?> createDesignation(
            @RequestBody Designation designation) {

        return ResponseEntity.ok(
                service.createDesignation(designation));
    }


    @GetMapping("/designations")
    @PreAuthorize("hasAuthority('DESIGNATION_VIEW')")
    public ResponseEntity<?> getAllDesignation() {

        return ResponseEntity.ok(
                service.getAllDesignation());
    }


    @PutMapping("/designation/{id}")
    @PreAuthorize("hasAuthority('DESIGNATION_UPDATE')")
    public ResponseEntity<?> updateDesignation(
            @PathVariable Long id,
            @RequestBody Designation designation) {

        return ResponseEntity.ok(
                service.updateDesignation(id, designation));
    }


    @DeleteMapping("/designation/{id}")
    @PreAuthorize("hasAuthority('DESIGNATION_DELETE')")
    public ResponseEntity<?> deleteDesignation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDesignation(id));
    }


    // ==============================
    // Team APIs
    // ==============================

    @PostMapping("/team")
    @PreAuthorize("hasAuthority('TEAM_CREATE')")
    public ResponseEntity<?> createTeam(
            @RequestBody Team team) {

        return ResponseEntity.ok(
                service.createTeam(team));
    }


    @GetMapping("/teams")
    @PreAuthorize("hasAuthority('TEAM_VIEW')")
    public ResponseEntity<?> getAllTeams() {

        return ResponseEntity.ok(
                service.getAllTeams());
    }


    @PutMapping("/team/{id}")
    @PreAuthorize("hasAuthority('TEAM_UPDATE')")
    public ResponseEntity<?> updateTeam(
            @PathVariable Long id,
            @RequestBody Team team) {

        return ResponseEntity.ok(
                service.updateTeam(id, team));
    }


    @DeleteMapping("/team/{id}")
    @PreAuthorize("hasAuthority('TEAM_DELETE')")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTeam(id));
    }


    // ==============================
    // Organization Hierarchy APIs
    // ==============================

    @PostMapping("/hierarchy")
    @PreAuthorize("hasAuthority('ORGANIZATION_HIERARCHY_CREATE')")
    public ResponseEntity<?> createHierarchy(
            @RequestBody OrganizationHierarchy hierarchy) {

        return ResponseEntity.ok(
                service.createHierarchy(hierarchy));
    }


    @GetMapping("/hierarchies")
    @PreAuthorize("hasAuthority('ORGANIZATION_HIERARCHY_VIEW')")
    public ResponseEntity<?> getAllHierarchies() {

        return ResponseEntity.ok(
                service.getAllHierarchies());
    }


    @PutMapping("/hierarchy/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_HIERARCHY_UPDATE')")
    public ResponseEntity<?> updateHierarchy(
            @PathVariable Long id,
            @RequestBody OrganizationHierarchy hierarchy) {

        return ResponseEntity.ok(
                service.updateHierarchy(id, hierarchy));
    }


    @DeleteMapping("/hierarchy/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_HIERARCHY_DELETE')")
    public ResponseEntity<?> deleteHierarchy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteHierarchy(id));
    }

}