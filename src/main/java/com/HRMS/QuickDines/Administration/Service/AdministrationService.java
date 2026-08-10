package com.HRMS.QuickDines.Administration.Service;

import com.HRMS.QuickDines.Administration.model.*;
import com.HRMS.QuickDines.Administration.repo.*;
import com.HRMS.QuickDines.Administration.repo.VendorRepository;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Administration.model.Vendor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final AssetRepository assetRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;
    private final VendorRepository vendorRepository;
    private final VisitorRepository visitorRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final OfficeExpenseRepository officeExpenseRepository;
    private final CompanyPolicyRepository companyPolicyRepository;
    private final AnnouncementRepository announcementRepository;
    private final EventRepository eventRepository;

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

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



// =========================================================
// 1. ASSETS
// =========================================================

    public String createAsset(
            Long companyId,
            Long branchId,
            Long vendorId,
            Asset asset) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException("Vendor Not Found"));

        asset.setCompany(company);
        asset.setBranch(branch);
        asset.setVendor(vendor);

        Asset saved =
                assetRepository.save(asset);

        String performedBy =
                getLoggedInEmployeeId();

        String newData =
                convertToJson(saved);

        // =====================================================
        // AUDIT CREATE
        // =====================================================

        auditLogsService.logCreate(
                "ASSET",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Asset created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ASSET",
                "ASSET",
                "Asset created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET",
                "AssetService",
                "Asset created successfully"
        );

        return "Asset Created Successfully";
    }


// =========================================================
// GET ALL ASSETS
// =========================================================

    public List<Asset> getAllAssets() {

        return assetRepository.findAll();
    }


// =========================================================
// GET ASSET BY ID
// =========================================================

    public Asset getAsset(Long id) {

        return assetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Asset Not Found"));
    }


// =========================================================
// UPDATE ASSET
// =========================================================

    public String updateAsset(
            Long id,
            Asset asset) {

        Asset existing =
                assetRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Not Found"));

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE
        // =====================================================

        existing.setAssetCode(
                asset.getAssetCode());

        existing.setAssetName(
                asset.getAssetName());

        existing.setAssetCategory(
                asset.getAssetCategory());

        existing.setSerialNumber(
                asset.getSerialNumber());

        existing.setPurchaseDate(
                asset.getPurchaseDate());

        existing.setPurchaseCost(
                asset.getPurchaseCost());

        existing.setStatus(
                asset.getStatus());


        Asset updated =
                assetRepository.save(existing);


        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "ASSET",
                String.valueOf(id),
                performedBy,
                null,
                "Asset updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_ASSET",
                "ASSET",
                "Asset updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET",
                "AssetService",
                "Asset updated successfully"
        );

        return "Asset Updated Successfully";
    }


// =========================================================
// DELETE ASSET
// =========================================================

    public String deleteAsset(Long id) {

        Asset existing =
                assetRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Not Found"));

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        assetRepository.delete(existing);


        // =====================================================
        // AUDIT DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "ASSET",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existing.getId().toString(),
                "Asset deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_ASSET",
                "ASSET",
                "Asset deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET",
                "AssetService",
                "Asset deleted successfully"
        );

        return "Asset Deleted Successfully";
    }


// =========================================================
// 2. ASSET ASSIGNMENTS
// =========================================================

    public String createAssetAssignment(
            Long assetId,
            String employeeId,
            String assignedById,
            AssetAssignment assignment) {

        Asset asset =
                assetRepository.findById(assetId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        Employee assignedBy =
                employeeRepository.findById(assignedById)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));

        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedBy(assignedBy);

        asset.setStatus("ASSIGNED");

        assetRepository.save(asset);

        AssetAssignment saved =
                assetAssignmentRepository.save(assignment);

        String performedBy =
                getLoggedInEmployeeId();

        String newData =
                convertToJson(saved);


        // =====================================================
        // AUDIT CREATE
        // =====================================================

        auditLogsService.logCreate(
                "ASSET_ASSIGNMENT",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Asset assigned successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ASSET_ASSIGNMENT",
                "ASSET_ASSIGNMENT",
                "Asset assigned successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET_ASSIGNMENT",
                "AssetService",
                "Asset assigned successfully"
        );

        return "Asset Assigned Successfully";
    }


// =========================================================
// GET ALL ASSET ASSIGNMENTS
// =========================================================

    public List<AssetAssignment> getAllAssetAssignments() {

        return assetAssignmentRepository.findAll();
    }


// =========================================================
// GET ASSET ASSIGNMENT BY ID
// =========================================================

    public AssetAssignment getAssetAssignment(Long id) {

        return assetAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Asset Assignment Not Found"));
    }


// =========================================================
// UPDATE ASSET ASSIGNMENT
// =========================================================

    public String updateAssetAssignment(
            Long id,
            AssetAssignment assignment) {

        AssetAssignment existing =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Assignment Not Found"));

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE
        // =====================================================

        existing.setAssignedDate(
                assignment.getAssignedDate());

        existing.setReturnDate(
                assignment.getReturnDate());

        existing.setAssignmentStatus(
                assignment.getAssignmentStatus());

        existing.setRemarks(
                assignment.getRemarks());


        AssetAssignment updated =
                assetAssignmentRepository.save(existing);


        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "ASSET_ASSIGNMENT",
                String.valueOf(id),
                performedBy,
                null,
                "Asset assignment updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_ASSET_ASSIGNMENT",
                "ASSET_ASSIGNMENT",
                "Asset assignment updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET_ASSIGNMENT",
                "AssetService",
                "Asset assignment updated successfully"
        );

        return "Asset Assignment Updated Successfully";
    }


// =========================================================
// DELETE ASSET ASSIGNMENT
// =========================================================

    public String deleteAssetAssignment(Long id) {

        AssetAssignment existing =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Assignment Not Found"));

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        assetAssignmentRepository.delete(existing);


        // =====================================================
        // AUDIT DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "ASSET_ASSIGNMENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existing.getId().toString(),
                "Asset assignment deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_ASSET_ASSIGNMENT",
                "ASSET_ASSIGNMENT",
                "Asset assignment deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ASSET_ASSIGNMENT",
                "AssetService",
                "Asset assignment deleted successfully"
        );

        return "Asset Assignment Deleted Successfully";
    }


// =========================================================
// 3. VENDORS
// =========================================================

    public String createVendor(Vendor vendor) {

        Vendor saved =
                vendorRepository.save(vendor);

        String performedBy =
                getLoggedInEmployeeId();

        String newData =
                convertToJson(saved);


        // =====================================================
        // AUDIT CREATE
        // =====================================================

        auditLogsService.logCreate(
                "VENDOR",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Vendor created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_VENDOR",
                "VENDOR",
                "Vendor created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VENDOR",
                "AssetService",
                "Vendor created successfully"
        );

        return "Vendor Created Successfully";
    }


// =========================================================
// GET ALL VENDORS
// =========================================================

    public List<Vendor> getAllVendors() {

        return vendorRepository.findAll();
    }


// =========================================================
// GET VENDOR BY ID
// =========================================================

    public Vendor getVendor(Long id) {

        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vendor Not Found"));
    }


// =========================================================
// UPDATE VENDOR
// =========================================================

    public String updateVendor(
            Long id,
            Vendor vendor) {

        Vendor existing =
                getVendor(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);


        // =====================================================
        // UPDATE
        // =====================================================

        existing.setVendorCode(
                vendor.getVendorCode());

        existing.setVendorName(
                vendor.getVendorName());

        existing.setContactPerson(
                vendor.getContactPerson());

        existing.setEmail(
                vendor.getEmail());

        existing.setPhone(
                vendor.getPhone());

        existing.setGstNumber(
                vendor.getGstNumber());

        existing.setAddress(
                vendor.getAddress());

        existing.setCity(
                vendor.getCity());

        existing.setState(
                vendor.getState());

        existing.setStatus(
                vendor.getStatus());


        Vendor updated =
                vendorRepository.save(existing);


        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // AUDIT UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "VENDOR",
                String.valueOf(id),
                performedBy,
                null,
                "Vendor updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_VENDOR",
                "VENDOR",
                "Vendor updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VENDOR",
                "AssetService",
                "Vendor updated successfully"
        );

        return "Vendor Updated Successfully";
    }


// =========================================================
// DELETE VENDOR
// =========================================================

    public String deleteVendor(Long id) {

        Vendor existing =
                getVendor(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();


        // =====================================================
        // DELETE
        // =====================================================

        vendorRepository.delete(existing);


        // =====================================================
        // AUDIT DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "VENDOR",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existing.getId().toString(),
                "Vendor deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_VENDOR",
                "VENDOR",
                "Vendor deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VENDOR",
                "AssetService",
                "Vendor deleted successfully"
        );

        return "Vendor Deleted Successfully";
    }

// =========================================================
// 4. VISITORS
// =========================================================

    public String createVisitor(
            String employeeId,
            Visitor visitor) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        visitor.setEmployee(employee);

        Visitor savedVisitor =
                visitorRepository.save(visitor);

        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        String newValue =
                convertToJson(savedVisitor);

        auditLogsService.logCreate(
                "VISITOR",
                String.valueOf(savedVisitor.getId()),
                performedBy,
                savedVisitor.getId().toString(),
                "Visitor created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_VISITOR",
                "VISITOR",
                "Visitor created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VISITOR",
                "VisitorService",
                "Visitor created successfully"
        );

        return "Visitor Created Successfully";
    }


    public List<Visitor> getAllVisitors() {

        return visitorRepository.findAll();
    }


    public Visitor getVisitor(Long id) {

        return visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Visitor Not Found"));
    }


    public String updateVisitor(
            Long id,
            Visitor visitor) {

        Visitor existing =
                getVisitor(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldValue =
                convertToJson(existing);

        // =====================================================
        // UPDATE
        // =====================================================

        existing.setVisitorName(
                visitor.getVisitorName());

        existing.setCompanyName(
                visitor.getCompanyName());

        existing.setMobileNumber(
                visitor.getMobileNumber());

        existing.setEmail(
                visitor.getEmail());

        existing.setPurpose(
                visitor.getPurpose());

        existing.setCheckIn(
                visitor.getCheckIn());

        existing.setCheckOut(
                visitor.getCheckOut());

        existing.setVisitorStatus(
                visitor.getVisitorStatus());


        Visitor updatedVisitor =
                visitorRepository.save(existing);


        // =====================================================
        // AUDIT
        // =====================================================

        String newValue =
                convertToJson(updatedVisitor);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "VISITOR",
                String.valueOf(id),
                performedBy,
                null,
                "Visitor updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_VISITOR",
                "VISITOR",
                "Visitor updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VISITOR",
                "VisitorService",
                "Visitor updated successfully"
        );

        return "Visitor Updated Successfully";
    }


    public String deleteVisitor(Long id) {

        Visitor visitor =
                getVisitor(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(visitor);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        visitorRepository.delete(visitor);

        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.createAuditLog(
                "VISITOR",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                visitor.getId().toString(),
                "Visitor deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_VISITOR",
                "VISITOR",
                "Visitor deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "VISITOR",
                "VisitorService",
                "Visitor deleted successfully"
        );

        return "Visitor Deleted Successfully";
    }


// =========================================================
// 5. INVENTORY
// =========================================================

    public String createInventory(
            Long vendorId,
            Inventory inventory) {

        Vendor vendor =
                vendorRepository.findById(vendorId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vendor Not Found"));

        inventory.setVendor(vendor);

        Inventory savedInventory =
                inventoryRepository.save(inventory);


        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        String newValue =
                convertToJson(savedInventory);

        auditLogsService.logCreate(
                "INVENTORY",
                String.valueOf(savedInventory.getId()),
                performedBy,
                savedInventory.getId().toString(),
                "Inventory created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_INVENTORY",
                "INVENTORY",
                "Inventory created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY",
                "InventoryService",
                "Inventory created successfully"
        );

        return "Inventory Created Successfully";
    }


    public List<Inventory> getAllInventory() {

        return inventoryRepository.findAll();
    }


    public Inventory getInventory(Long id) {

        return inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory Not Found"));
    }


    public String updateInventory(
            Long id,
            Inventory inventory) {

        Inventory existing =
                getInventory(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldValue =
                convertToJson(existing);


        // =====================================================
        // UPDATE
        // =====================================================

        existing.setItemCode(
                inventory.getItemCode());

        existing.setItemName(
                inventory.getItemName());

        existing.setCategory(
                inventory.getCategory());

        existing.setUnit(
                inventory.getUnit());

        existing.setQuantity(
                inventory.getQuantity());

        existing.setMinimumQuantity(
                inventory.getMinimumQuantity());

        existing.setWarehouseLocation(
                inventory.getWarehouseLocation());

        existing.setStatus(
                inventory.getStatus());


        Inventory updatedInventory =
                inventoryRepository.save(existing);


        // =====================================================
        // AUDIT
        // =====================================================

        String newValue =
                convertToJson(updatedInventory);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "INVENTORY",
                String.valueOf(id),
                performedBy,
                null,
                "Inventory updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_INVENTORY",
                "INVENTORY",
                "Inventory updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY",
                "InventoryService",
                "Inventory updated successfully"
        );

        return "Inventory Updated Successfully";
    }


    public String deleteInventory(Long id) {

        Inventory inventory =
                getInventory(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(inventory);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        inventoryRepository.delete(inventory);


        // =====================================================
        // AUDIT
        // =====================================================

        auditLogsService.createAuditLog(
                "INVENTORY",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                inventory.getId().toString(),
                "Inventory deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_INVENTORY",
                "INVENTORY",
                "Inventory deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY",
                "InventoryService",
                "Inventory deleted successfully"
        );

        return "Inventory Deleted Successfully";
    }


    // =========================================================
// 6. INVENTORY TRANSACTIONS
// =========================================================

    public String createInventoryTransaction(
            Long inventoryId,
            String employeeId,
            InventoryTransaction transaction) {

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory Not Found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        transaction.setInventory(inventory);
        transaction.setEmployee(employee);

        Integer quantity = transaction.getQuantity();

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Transaction quantity must be greater than zero");
        }

        if ("IN".equalsIgnoreCase(
                transaction.getTransactionType())) {

            inventory.setQuantity(
                    inventory.getQuantity() + quantity);

        } else if ("OUT".equalsIgnoreCase(
                transaction.getTransactionType())) {

            if (inventory.getQuantity() < quantity) {
                throw new RuntimeException(
                        "Insufficient Inventory");
            }

            inventory.setQuantity(
                    inventory.getQuantity() - quantity);

        } else if ("RETURN".equalsIgnoreCase(
                transaction.getTransactionType())) {

            inventory.setQuantity(
                    inventory.getQuantity() + quantity);

        } else {

            throw new RuntimeException(
                    "Invalid Transaction Type. Use IN, OUT or RETURN");
        }

        inventoryRepository.save(inventory);

        InventoryTransaction saved =
                inventoryTransactionRepository.save(transaction);

        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "INVENTORY_TRANSACTION",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Inventory transaction created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_INVENTORY_TRANSACTION",
                "INVENTORY_TRANSACTION",
                "Inventory transaction created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY_TRANSACTION",
                "AssetManagementService",
                "Inventory transaction created successfully"
        );

        return "Inventory Transaction Created Successfully";
    }


    public List<InventoryTransaction>
    getAllInventoryTransactions() {

        return inventoryTransactionRepository.findAll();
    }


    public InventoryTransaction getInventoryTransaction(
            Long id) {

        return inventoryTransactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory Transaction Not Found"));
    }


    public String updateInventoryTransaction(
            Long id,
            InventoryTransaction transaction) {

        InventoryTransaction existing =
                getInventoryTransaction(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        // =====================================================
        // UPDATE DATA
        // =====================================================

        existing.setTransactionType(
                transaction.getTransactionType());

        existing.setQuantity(
                transaction.getQuantity());

        existing.setTransactionDate(
                transaction.getTransactionDate());

        existing.setRemarks(
                transaction.getRemarks());

        InventoryTransaction updated =
                inventoryTransactionRepository.save(existing);

        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "INVENTORY_TRANSACTION",
                String.valueOf(id),
                performedBy,
                null,
                "Inventory transaction updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_INVENTORY_TRANSACTION",
                "INVENTORY_TRANSACTION",
                "Inventory transaction updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY_TRANSACTION",
                "AssetManagementService",
                "Inventory transaction updated successfully"
        );

        return "Inventory Transaction Updated Successfully";
    }


    public String deleteInventoryTransaction(Long id) {

        InventoryTransaction transaction =
                getInventoryTransaction(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(transaction);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        inventoryTransactionRepository.delete(transaction);

        // =====================================================
        // AUDIT DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "INVENTORY_TRANSACTION",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Inventory transaction deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_INVENTORY_TRANSACTION",
                "INVENTORY_TRANSACTION",
                "Inventory transaction deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "INVENTORY_TRANSACTION",
                "AssetManagementService",
                "Inventory transaction deleted successfully"
        );

        return "Inventory Transaction Deleted Successfully";
    }
    // =========================================================
// 7. OFFICE EXPENSES
// =========================================================

    public String createOfficeExpense(
            Long vendorId,
            String approvedById,
            OfficeExpense expense) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException("Vendor Not Found"));

        Employee approvedBy =
                employeeRepository.findById(approvedById)
                        .orElseThrow(() ->
                                new RuntimeException("User Not Found"));

        expense.setVendor(vendor);
        expense.setApprovedBy(approvedBy);

        OfficeExpense saved =
                officeExpenseRepository.save(expense);

        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "OFFICE_EXPENSE",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Office expense created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_OFFICE_EXPENSE",
                "OFFICE_EXPENSE",
                "Office expense created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "OFFICE_EXPENSE",
                "AssetManagementService",
                "Office expense created successfully"
        );

        return "Office Expense Created Successfully";
    }


    public List<OfficeExpense> getAllOfficeExpenses() {

        return officeExpenseRepository.findAll();
    }


    public OfficeExpense getOfficeExpense(Long id) {

        return officeExpenseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Office Expense Not Found"));
    }


    public String updateOfficeExpense(
            Long id,
            OfficeExpense expense) {

        OfficeExpense existing =
                getOfficeExpense(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        // =====================================================
        // UPDATE DATA
        // =====================================================

        existing.setExpenseCategory(
                expense.getExpenseCategory());

        existing.setExpenseDate(
                expense.getExpenseDate());

        existing.setAmount(
                expense.getAmount());

        existing.setPaymentMode(
                expense.getPaymentMode());

        existing.setRemarks(
                expense.getRemarks());

        OfficeExpense updated =
                officeExpenseRepository.save(existing);

        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "OFFICE_EXPENSE",
                String.valueOf(id),
                performedBy,
                null,
                "Office expense updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_OFFICE_EXPENSE",
                "OFFICE_EXPENSE",
                "Office expense updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "OFFICE_EXPENSE",
                "AssetManagementService",
                "Office expense updated successfully"
        );

        return "Office Expense Updated Successfully";
    }


    public String deleteOfficeExpense(Long id) {

        OfficeExpense expense =
                getOfficeExpense(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(expense);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        officeExpenseRepository.delete(expense);

        // =====================================================
        // AUDIT DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "OFFICE_EXPENSE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Office expense deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_OFFICE_EXPENSE",
                "OFFICE_EXPENSE",
                "Office expense deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "OFFICE_EXPENSE",
                "AssetManagementService",
                "Office expense deleted successfully"
        );

        return "Office Expense Deleted Successfully";
    }


// =========================================================
// 9. ANNOUNCEMENTS
// =========================================================

    public String createAnnouncement(
            Long companyId,
            Long branchId,
            String postedById,
            Announcement announcement) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        Employee postedBy = employeeRepository.findById(postedById)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        announcement.setCompany(company);
        announcement.setBranch(branch);
        announcement.setPostedBy(postedBy);

        Announcement saved =
                announcementRepository.save(announcement);

        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "ANNOUNCEMENT",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Announcement created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ANNOUNCEMENT",
                "ANNOUNCEMENT",
                "Announcement created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ANNOUNCEMENT",
                "AssetManagementService",
                "Announcement created successfully"
        );

        return "Announcement Created Successfully";
    }


    public List<Announcement> getAllAnnouncements() {

        return announcementRepository.findAll();
    }


    public Announcement getAnnouncement(Long id) {

        return announcementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Announcement Not Found"));
    }


    public String updateAnnouncement(
            Long id,
            Announcement announcement) {

        Announcement existing =
                getAnnouncement(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        // =====================================================
        // UPDATE
        // =====================================================

        existing.setTitle(
                announcement.getTitle());

        existing.setDescription(
                announcement.getDescription());

        existing.setPublishDate(
                announcement.getPublishDate());

        existing.setExpiryDate(
                announcement.getExpiryDate());

        existing.setPriority(
                announcement.getPriority());

        Announcement updated =
                announcementRepository.save(existing);

        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "ANNOUNCEMENT",
                String.valueOf(id),
                performedBy,
                null,
                "Announcement updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_ANNOUNCEMENT",
                "ANNOUNCEMENT",
                "Announcement updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ANNOUNCEMENT",
                "AssetManagementService",
                "Announcement updated successfully"
        );

        return "Announcement Updated Successfully";
    }


    public String deleteAnnouncement(Long id) {

        Announcement announcement =
                getAnnouncement(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(announcement);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        announcementRepository.delete(announcement);

        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "ANNOUNCEMENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Announcement deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_ANNOUNCEMENT",
                "ANNOUNCEMENT",
                "Announcement deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "ANNOUNCEMENT",
                "AssetManagementService",
                "Announcement deleted successfully"
        );

        return "Announcement Deleted Successfully";
    }


// =========================================================
// 10. EVENTS
// =========================================================

    public String createEvent(
            Long companyId,
            Long branchId,
            String organizerId,
            Event event) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch Not Found"));

        Employee organizer =
                employeeRepository.findById(organizerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organizer Employee Not Found"));

        event.setCompany(company);
        event.setBranch(branch);
        event.setOrganizer(organizer);

        Event saved =
                eventRepository.save(event);

        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EVENT",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Event created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_EVENT",
                "EVENT",
                "Event created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EVENT",
                "AssetManagementService",
                "Event created successfully"
        );

        return "Event Created Successfully";
    }


    public List<Event> getAllEvents() {

        return eventRepository.findAll();
    }


    public Event getEvent(Long id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event Not Found"));
    }


    public String updateEvent(
            Long id,
            Event event) {

        Event existing =
                getEvent(id);

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        // =====================================================
        // UPDATE
        // =====================================================

        existing.setEventName(
                event.getEventName());

        existing.setEventType(
                event.getEventType());

        existing.setEventDate(
                event.getEventDate());

        existing.setStartTime(
                event.getStartTime());

        existing.setEndTime(
                event.getEndTime());

        existing.setVenue(
                event.getVenue());

        existing.setDescription(
                event.getDescription());

        Event updated =
                eventRepository.save(existing);

        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "EVENT",
                String.valueOf(id),
                performedBy,
                null,
                "Event updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_EVENT",
                "EVENT",
                "Event updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EVENT",
                "AssetManagementService",
                "Event updated successfully"
        );

        return "Event Updated Successfully";
    }


    public String deleteEvent(Long id) {

        Event event =
                getEvent(id);

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String oldData =
                convertToJson(event);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        eventRepository.delete(event);

        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "EVENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Event deleted successfully",
                oldData,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_EVENT",
                "EVENT",
                "Event deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EVENT",
                "AssetManagementService",
                "Event deleted successfully"
        );

        return "Event Deleted Successfully";
    }

// =========================================================
// COMPANY POLICIES
// =========================================================

// =========================================================
// CREATE COMPANY POLICY
// =========================================================

    public String createCompanyPolicy(
            Long companyId,
            CompanyPolicy policy) {

        Company company =
                companyRepository.findById(companyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Company Not Found"));

        policy.setCompany(company);

        CompanyPolicy saved =
                companyPolicyRepository.save(policy);

        // =====================================================
        // AUDIT INFORMATION
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT - CREATE
        // =====================================================

        auditLogsService.logCreate(
                "COMPANY_POLICY",
                String.valueOf(saved.getId()),
                performedBy,
                String.valueOf(saved.getId()),
                "Company Policy created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_COMPANY_POLICY",
                "COMPANY_POLICY",
                "Company Policy created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "COMPANY_POLICY",
                "CompanyPolicyService",
                "Company Policy created successfully"
        );

        return "Company Policy Created Successfully";
    }


// =========================================================
// GET ALL COMPANY POLICIES
// =========================================================

    public List<CompanyPolicy> getAllCompanyPolicies() {

        return companyPolicyRepository.findAll();
    }


// =========================================================
// GET COMPANY POLICY BY ID
// =========================================================

    public CompanyPolicy getCompanyPolicy(Long id) {

        return companyPolicyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Company Policy Not Found"));
    }


// =========================================================
// UPDATE COMPANY POLICY
// =========================================================

    public String updateCompanyPolicy(
            Long id,
            CompanyPolicy policy) {

        CompanyPolicy existing =
                companyPolicyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Company Policy Not Found"));

        // =====================================================
        // OLD DATA
        // =====================================================

        String oldData =
                convertToJson(existing);

        // =====================================================
        // UPDATE DATA
        // =====================================================

        existing.setPolicyName(
                policy.getPolicyName());

        existing.setPolicyType(
                policy.getPolicyType());

        existing.setVersion(
                policy.getVersion());

        existing.setEffectiveDate(
                policy.getEffectiveDate());

        existing.setDocumentUrl(
                policy.getDocumentUrl());

        existing.setStatus(
                policy.getStatus());

        CompanyPolicy updated =
                companyPolicyRepository.save(existing);

        // =====================================================
        // NEW DATA
        // =====================================================

        String newData =
                convertToJson(updated);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT - UPDATE
        // =====================================================

        auditLogsService.logUpdate(
                "COMPANY_POLICY",
                String.valueOf(id),
                performedBy,
                policy.getId().toString(),
                "Company Policy updated successfully",
                oldData,
                newData
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_COMPANY_POLICY",
                "COMPANY_POLICY",
                "Company Policy updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "COMPANY_POLICY",
                "CompanyPolicyService",
                "Company Policy updated successfully"
        );

        return "Company Policy Updated Successfully";
    }


// =========================================================
// DELETE COMPANY POLICY
// =========================================================

    public String deleteCompanyPolicy(Long id) {

        CompanyPolicy policy =
                companyPolicyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Company Policy Not Found"));

        // =====================================================
        // OLD DATA BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(policy);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // DELETE
        // =====================================================

        companyPolicyRepository.delete(policy);

        // =====================================================
        // AUDIT - DELETE
        // =====================================================

        auditLogsService.createAuditLog(
                "COMPANY_POLICY",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Company Policy deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_COMPANY_POLICY",
                "COMPANY_POLICY",
                "Company Policy deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "COMPANY_POLICY",
                "CompanyPolicyService",
                "Company Policy deleted successfully"
        );

        return "Company Policy Deleted Successfully";
    }




}

