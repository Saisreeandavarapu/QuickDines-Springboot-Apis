package com.HRMS.QuickDines.Organization.Controller;

import com.HRMS.QuickDines.Organization.Service.OrganizationService;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Organization.model.OrganizationHierarchy;
import com.HRMS.QuickDines.Organization.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createDepartment(
            @RequestBody Department department) {

        return ResponseEntity.ok(
                service.createDepartment(department));
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {

        return ResponseEntity.ok(
                service.getAllDepartments());
    }

    @PutMapping("/department/{id}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {

        return ResponseEntity.ok(
                service.updateDepartment(id, department));
    }

    @DeleteMapping("/department/{id}")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDepartment(id));
    }


    // ==============================
    // Designation APIs
    // ==============================

    @PostMapping("/designation")
    public ResponseEntity<?> createDesignation(
            @RequestBody Designation designation) {

        return ResponseEntity.ok(
                service.createDesignation(designation));
    }

    @GetMapping("/designations")
    public ResponseEntity<?> getAllDesignation() {

        return ResponseEntity.ok(
                service.getAllDesignation());
    }

    @PutMapping("/designation/{id}")
    public ResponseEntity<?> updateDesignation(
            @PathVariable Long id,
            @RequestBody Designation designation) {

        return ResponseEntity.ok(
                service.updateDesignation(id, designation));
    }

    @DeleteMapping("/designation/{id}")
    public ResponseEntity<?> deleteDesignation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDesignation(id));
    }


    // ==============================
    // Team APIs
    // ==============================

    @PostMapping("/team")
    public ResponseEntity<?> createTeam(
            @RequestBody Team team) {

        return ResponseEntity.ok(
                service.createTeam(team));
    }

    @GetMapping("/teams")
    public ResponseEntity<?> getAllTeams() {

        return ResponseEntity.ok(
                service.getAllTeams());
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<?> updateTeam(
            @PathVariable Long id,
            @RequestBody Team team) {

        return ResponseEntity.ok(
                service.updateTeam(id, team));
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTeam(id));
    }


    // ==============================
    // Organization Hierarchy APIs
    // ==============================

    @PostMapping("/hierarchy")
    public ResponseEntity<?> createHierarchy(
            @RequestBody OrganizationHierarchy hierarchy) {

        return ResponseEntity.ok(
                service.createHierarchy(hierarchy));
    }

    @GetMapping("/hierarchies")
    public ResponseEntity<?> getAllHierarchies() {

        return ResponseEntity.ok(
                service.getAllHierarchies());
    }

    @PutMapping("/hierarchy/{id}")
    public ResponseEntity<?> updateHierarchy(
            @PathVariable Long id,
            @RequestBody OrganizationHierarchy hierarchy) {

        return ResponseEntity.ok(
                service.updateHierarchy(id, hierarchy));
    }

    @DeleteMapping("/hierarchy/{id}")
    public ResponseEntity<?> deleteHierarchy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteHierarchy(id));
    }

}
