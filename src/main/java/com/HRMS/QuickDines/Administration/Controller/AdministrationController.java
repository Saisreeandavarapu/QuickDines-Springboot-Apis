package com.HRMS.QuickDines.Administration.Controller;

import com.HRMS.QuickDines.Administration.Service.AdministrationService;
import com.HRMS.QuickDines.Administration.model.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administration")
@RequiredArgsConstructor
public class AdministrationController {

    private final AdministrationService service;


    // =========================================================
    // ASSETS
    // =========================================================

    @PostMapping("/asset/{companyId}/{branchId}/{vendorId}")
    @PreAuthorize("hasAuthority('ASSET_CREATE')")
    public ResponseEntity<?> createAsset(
            @PathVariable Long companyId,
            @PathVariable Long branchId,
            @PathVariable Long vendorId,
            @RequestBody Asset asset) {

        return ResponseEntity.ok(
                service.createAsset(
                        companyId,
                        branchId,
                        vendorId,
                        asset));
    }


    @GetMapping("/assets")
    @PreAuthorize("hasAuthority('ASSET_READ')")
    public ResponseEntity<?> getAllAssets() {

        return ResponseEntity.ok(
                service.getAllAssets());
    }


    @GetMapping("/asset/{id}")
    @PreAuthorize("hasAuthority('ASSET_READ')")
    public ResponseEntity<?> getAsset(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAsset(id));
    }


    @PutMapping("/asset/{id}")
    @PreAuthorize("hasAuthority('ASSET_UPDATE')")
    public ResponseEntity<?> updateAsset(
            @PathVariable Long id,
            @RequestBody Asset asset) {

        return ResponseEntity.ok(
                service.updateAsset(id, asset));
    }


    @DeleteMapping("/asset/{id}")
    @PreAuthorize("hasAuthority('ASSET_DELETE')")
    public ResponseEntity<?> deleteAsset(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAsset(id));
    }


    // =========================================================
    // ASSET ASSIGNMENTS
    // =========================================================

    @PostMapping("/asset-assignment/{assetId}/{employeeId}/{assignedById}")
    @PreAuthorize("hasAuthority('ASSET_ASSIGNMENT_CREATE')")
    public ResponseEntity<?> createAssetAssignment(
            @PathVariable Long assetId,
            @PathVariable String employeeId,
            @PathVariable String assignedById,
            @RequestBody AssetAssignment assignment) {

        return ResponseEntity.ok(
                service.createAssetAssignment(
                        assetId,
                        employeeId,
                        assignedById,
                        assignment));
    }


    @GetMapping("/asset-assignments")
    @PreAuthorize("hasAuthority('ASSET_ASSIGNMENT_READ')")
    public ResponseEntity<?> getAllAssetAssignments() {

        return ResponseEntity.ok(
                service.getAllAssetAssignments());
    }


    @GetMapping("/asset-assignment/{id}")
    @PreAuthorize("hasAuthority('ASSET_ASSIGNMENT_READ')")
    public ResponseEntity<?> getAssetAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAssetAssignment(id));
    }


    @PutMapping("/asset-assignment/{id}")
    @PreAuthorize("hasAuthority('ASSET_ASSIGNMENT_UPDATE')")
    public ResponseEntity<?> updateAssetAssignment(
            @PathVariable Long id,
            @RequestBody AssetAssignment assignment) {

        return ResponseEntity.ok(
                service.updateAssetAssignment(id, assignment));
    }


    @DeleteMapping("/asset-assignment/{id}")
    @PreAuthorize("hasAuthority('ASSET_ASSIGNMENT_DELETE')")
    public ResponseEntity<?> deleteAssetAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAssetAssignment(id));
    }


    // =========================================================
    // VENDORS
    // =========================================================

    @PostMapping("/vendor")
    @PreAuthorize("hasAuthority('VENDOR_CREATE')")
    public ResponseEntity<?> createVendor(
            @RequestBody Vendor vendor) {

        return ResponseEntity.ok(
                service.createVendor(vendor));
    }


    @GetMapping("/vendors")
    @PreAuthorize("hasAuthority('VENDOR_READ')")
    public ResponseEntity<?> getAllVendors() {

        return ResponseEntity.ok(
                service.getAllVendors());
    }


    @GetMapping("/vendor/{id}")
    @PreAuthorize("hasAuthority('VENDOR_READ')")
    public ResponseEntity<?> getVendor(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getVendor(id));
    }


    @PutMapping("/vendor/{id}")
    @PreAuthorize("hasAuthority('VENDOR_UPDATE')")
    public ResponseEntity<?> updateVendor(
            @PathVariable Long id,
            @RequestBody Vendor vendor) {

        return ResponseEntity.ok(
                service.updateVendor(id, vendor));
    }


    @DeleteMapping("/vendor/{id}")
    @PreAuthorize("hasAuthority('VENDOR_DELETE')")
    public ResponseEntity<?> deleteVendor(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteVendor(id));
    }


    // =========================================================
    // VISITORS
    // =========================================================

    @PostMapping("/visitor/{employeeId}")
    @PreAuthorize("hasAuthority('VISITOR_CREATE')")
    public ResponseEntity<?> createVisitor(
            @PathVariable String employeeId,
            @RequestBody Visitor visitor) {

        return ResponseEntity.ok(
                service.createVisitor(employeeId, visitor));
    }


    @GetMapping("/visitors")
    @PreAuthorize("hasAuthority('VISITOR_READ')")
    public ResponseEntity<?> getAllVisitors() {

        return ResponseEntity.ok(
                service.getAllVisitors());
    }


    @GetMapping("/visitor/{id}")
    @PreAuthorize("hasAuthority('VISITOR_READ')")
    public ResponseEntity<?> getVisitor(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getVisitor(id));
    }


    @PutMapping("/visitor/{id}")
    @PreAuthorize("hasAuthority('VISITOR_UPDATE')")
    public ResponseEntity<?> updateVisitor(
            @PathVariable Long id,
            @RequestBody Visitor visitor) {

        return ResponseEntity.ok(
                service.updateVisitor(id, visitor));
    }


    @DeleteMapping("/visitor/{id}")
    @PreAuthorize("hasAuthority('VISITOR_DELETE')")
    public ResponseEntity<?> deleteVisitor(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteVisitor(id));
    }


    // =========================================================
    // INVENTORY
    // =========================================================

    @PostMapping("/inventory/{vendorId}")
    @PreAuthorize("hasAuthority('INVENTORY_CREATE')")
    public ResponseEntity<?> createInventory(
            @PathVariable Long vendorId,
            @RequestBody Inventory inventory) {

        return ResponseEntity.ok(
                service.createInventory(vendorId, inventory));
    }


    @GetMapping("/inventories")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<?> getAllInventory() {

        return ResponseEntity.ok(
                service.getAllInventory());
    }


    @GetMapping("/inventory/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<?> getInventory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInventory(id));
    }


    @PutMapping("/inventory/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public ResponseEntity<?> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventory) {

        return ResponseEntity.ok(
                service.updateInventory(id, inventory));
    }


    @DeleteMapping("/inventory/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_DELETE')")
    public ResponseEntity<?> deleteInventory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInventory(id));
    }


    // =========================================================
    // INVENTORY TRANSACTIONS
    // =========================================================

    @PostMapping("/inventory-transaction/{inventoryId}/{employeeId}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSACTION_CREATE')")
    public ResponseEntity<?> createInventoryTransaction(
            @PathVariable Long inventoryId,
            @PathVariable String employeeId,
            @RequestBody InventoryTransaction transaction) {

        return ResponseEntity.ok(
                service.createInventoryTransaction(
                        inventoryId,
                        employeeId,
                        transaction));
    }


    @GetMapping("/inventory-transactions")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSACTION_READ')")
    public ResponseEntity<?> getAllInventoryTransactions() {

        return ResponseEntity.ok(
                service.getAllInventoryTransactions());
    }


    @GetMapping("/inventory-transaction/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSACTION_READ')")
    public ResponseEntity<?> getInventoryTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInventoryTransaction(id));
    }


    @PutMapping("/inventory-transaction/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSACTION_UPDATE')")
    public ResponseEntity<?> updateInventoryTransaction(
            @PathVariable Long id,
            @RequestBody InventoryTransaction transaction) {

        return ResponseEntity.ok(
                service.updateInventoryTransaction(id, transaction));
    }


    @DeleteMapping("/inventory-transaction/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSACTION_DELETE')")
    public ResponseEntity<?> deleteInventoryTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInventoryTransaction(id));
    }


    // =========================================================
    // OFFICE EXPENSES
    // =========================================================

    @PostMapping("/office-expense/{vendorId}/{approvedById}")
    @PreAuthorize("hasAuthority('OFFICE_EXPENSE_CREATE')")
    public ResponseEntity<?> createOfficeExpense(
            @PathVariable Long vendorId,
            @PathVariable String approvedById,
            @RequestBody OfficeExpense expense) {

        return ResponseEntity.ok(
                service.createOfficeExpense(
                        vendorId,
                        approvedById,
                        expense));
    }


    @GetMapping("/office-expenses")
    @PreAuthorize("hasAuthority('OFFICE_EXPENSE_READ')")
    public ResponseEntity<?> getAllOfficeExpenses() {

        return ResponseEntity.ok(
                service.getAllOfficeExpenses());
    }


    @GetMapping("/office-expense/{id}")
    @PreAuthorize("hasAuthority('OFFICE_EXPENSE_READ')")
    public ResponseEntity<?> getOfficeExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getOfficeExpense(id));
    }


    @PutMapping("/office-expense/{id}")
    @PreAuthorize("hasAuthority('OFFICE_EXPENSE_UPDATE')")
    public ResponseEntity<?> updateOfficeExpense(
            @PathVariable Long id,
            @RequestBody OfficeExpense expense) {

        return ResponseEntity.ok(
                service.updateOfficeExpense(id, expense));
    }


    @DeleteMapping("/office-expense/{id}")
    @PreAuthorize("hasAuthority('OFFICE_EXPENSE_DELETE')")
    public ResponseEntity<?> deleteOfficeExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteOfficeExpense(id));
    }


    // =========================================================
    // COMPANY POLICIES
    // =========================================================

    @PostMapping("/company-policy/{companyId}")
    @PreAuthorize("hasAuthority('COMPANY_POLICY_CREATE')")
    public ResponseEntity<?> createCompanyPolicy(
            @PathVariable Long companyId,
            @RequestBody CompanyPolicy policy) {

        return ResponseEntity.ok(
                service.createCompanyPolicy(companyId, policy));
    }


    @GetMapping("/company-policies")
    @PreAuthorize("hasAuthority('COMPANY_POLICY_READ')")
    public ResponseEntity<?> getAllCompanyPolicies() {

        return ResponseEntity.ok(
                service.getAllCompanyPolicies());
    }


    @GetMapping("/company-policy/{id}")
    @PreAuthorize("hasAuthority('COMPANY_POLICY_READ')")
    public ResponseEntity<?> getCompanyPolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCompanyPolicy(id));
    }


    @PutMapping("/company-policy/{id}")
    @PreAuthorize("hasAuthority('COMPANY_POLICY_UPDATE')")
    public ResponseEntity<?> updateCompanyPolicy(
            @PathVariable Long id,
            @RequestBody CompanyPolicy policy) {

        return ResponseEntity.ok(
                service.updateCompanyPolicy(id, policy));
    }


    @DeleteMapping("/company-policy/{id}")
    @PreAuthorize("hasAuthority('COMPANY_POLICY_DELETE')")
    public ResponseEntity<?> deleteCompanyPolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCompanyPolicy(id));
    }


    // =========================================================
    // ANNOUNCEMENTS
    // =========================================================

    @PostMapping("/announcement/{companyId}/{branchId}/{postedById}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_CREATE')")
    public ResponseEntity<?> createAnnouncement(
            @PathVariable Long companyId,
            @PathVariable Long branchId,
            @PathVariable String postedById,
            @RequestBody Announcement announcement) {

        return ResponseEntity.ok(
                service.createAnnouncement(
                        companyId,
                        branchId,
                        postedById,
                        announcement));
    }


    @GetMapping("/announcements")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_READ')")
    public ResponseEntity<?> getAllAnnouncements() {

        return ResponseEntity.ok(
                service.getAllAnnouncements());
    }


    @GetMapping("/announcement/{id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_READ')")
    public ResponseEntity<?> getAnnouncement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAnnouncement(id));
    }


    @PutMapping("/announcement/{id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_UPDATE')")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody Announcement announcement) {

        return ResponseEntity.ok(
                service.updateAnnouncement(id, announcement));
    }


    @DeleteMapping("/announcement/{id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_DELETE')")
    public ResponseEntity<?> deleteAnnouncement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAnnouncement(id));
    }


    // =========================================================
    // EVENTS
    // =========================================================

    @PostMapping("/event/{companyId}/{branchId}/{organizerId}")
    @PreAuthorize("hasAuthority('EVENT_CREATE')")
    public ResponseEntity<?> createEvent(
            @PathVariable Long companyId,
            @PathVariable Long branchId,
            @PathVariable String organizerId,
            @RequestBody Event event) {

        return ResponseEntity.ok(
                service.createEvent(
                        companyId,
                        branchId,
                        organizerId,
                        event));
    }


    @GetMapping("/events")
    @PreAuthorize("hasAuthority('EVENT_READ')")
    public ResponseEntity<?> getAllEvents() {

        return ResponseEntity.ok(
                service.getAllEvents());
    }


    @GetMapping("/event/{id}")
    @PreAuthorize("hasAuthority('EVENT_READ')")
    public ResponseEntity<?> getEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEvent(id));
    }


    @PutMapping("/event/{id}")
    @PreAuthorize("hasAuthority('EVENT_UPDATE')")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {

        return ResponseEntity.ok(
                service.updateEvent(id, event));
    }


    @DeleteMapping("/event/{id}")
    @PreAuthorize("hasAuthority('EVENT_DELETE')")
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteEvent(id));
    }
}