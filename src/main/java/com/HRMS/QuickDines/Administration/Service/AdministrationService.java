package com.HRMS.QuickDines.Administration.Service;

import com.HRMS.QuickDines.Administration.model.*;
import com.HRMS.QuickDines.Administration.repo.*;
import com.HRMS.QuickDines.Administration.repo.VendorRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Administration.model.Vendor;

import lombok.RequiredArgsConstructor;
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

        assetRepository.save(asset);

        return "Asset Created Successfully";
    }


    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }


    public Asset getAsset(Long id) {

        return assetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Asset Not Found"));
    }


    public String updateAsset(Long id, Asset asset) {

        Asset existing = assetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Asset Not Found"));

        existing.setAssetCode(asset.getAssetCode());
        existing.setAssetName(asset.getAssetName());
        existing.setAssetCategory(asset.getAssetCategory());
        existing.setSerialNumber(asset.getSerialNumber());
        existing.setPurchaseDate(asset.getPurchaseDate());
        existing.setPurchaseCost(asset.getPurchaseCost());
        existing.setStatus(asset.getStatus());

        assetRepository.save(existing);

        return "Asset Updated Successfully";
    }


    public String deleteAsset(Long id) {

        Asset existing = getAsset(id);

        assetRepository.delete(existing);

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

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new RuntimeException("Asset Not Found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        Employee assignedBy = EmployeeRepository.findByEmployeeId(assignedById).orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedBy(assignedBy);

        asset.setStatus("ASSIGNED");
        assetRepository.save(asset);

        assetAssignmentRepository.save(assignment);

        return "Asset Assigned Successfully";
    }


    public List<AssetAssignment> getAllAssetAssignments() {
        return assetAssignmentRepository.findAll();
    }


    public AssetAssignment getAssetAssignment(Long id) {

        return assetAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Asset Assignment Not Found"));
    }


    public String updateAssetAssignment(
            Long id,
            AssetAssignment assignment) {

        AssetAssignment existing =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset Assignment Not Found"));

        existing.setAssignedDate(assignment.getAssignedDate());
        existing.setReturnDate(assignment.getReturnDate());
        existing.setAssignmentStatus(
                assignment.getAssignmentStatus());
        existing.setRemarks(assignment.getRemarks());

        assetAssignmentRepository.save(existing);

        return "Asset Assignment Updated Successfully";
    }


    public String deleteAssetAssignment(Long id) {

        AssetAssignment existing = getAssetAssignment(id);

        assetAssignmentRepository.delete(existing);

        return "Asset Assignment Deleted Successfully";
    }


    // =========================================================
    // 3. VENDORS
    // =========================================================

    public String createVendor(Vendor vendor) {

        vendorRepository.save(vendor);

        return "Vendor Created Successfully";
    }


    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }


    public Vendor getVendor(Long id) {

        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vendor Not Found"));
    }


    public String updateVendor(Long id, Vendor vendor) {

        Vendor existing = getVendor(id);

        existing.setVendorCode(vendor.getVendorCode());
        existing.setVendorName(vendor.getVendorName());
        existing.setContactPerson(vendor.getContactPerson());
        existing.setEmail(vendor.getEmail());
        existing.setPhone(vendor.getPhone());
        existing.setGstNumber(vendor.getGstNumber());
        existing.setAddress(vendor.getAddress());
        existing.setCity(vendor.getCity());
        existing.setState(vendor.getState());
        existing.setStatus(vendor.getStatus());

        vendorRepository.save(existing);

        return "Vendor Updated Successfully";
    }


    public String deleteVendor(Long id) {

        Vendor vendor = getVendor(id);

        vendorRepository.delete(vendor);

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

        visitorRepository.save(visitor);

        return "Visitor Created Successfully";
    }


    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }


    public Visitor getVisitor(Long id) {

        return visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Visitor Not Found"));
    }


    public String updateVisitor(Long id, Visitor visitor) {

        Visitor existing = getVisitor(id);

        existing.setVisitorName(visitor.getVisitorName());
        existing.setCompanyName(visitor.getCompanyName());
        existing.setMobileNumber(visitor.getMobileNumber());
        existing.setEmail(visitor.getEmail());
        existing.setPurpose(visitor.getPurpose());
        existing.setCheckIn(visitor.getCheckIn());
        existing.setCheckOut(visitor.getCheckOut());
        existing.setVisitorStatus(visitor.getVisitorStatus());

        visitorRepository.save(existing);

        return "Visitor Updated Successfully";
    }


    public String deleteVisitor(Long id) {

        Visitor visitor = getVisitor(id);

        visitorRepository.delete(visitor);

        return "Visitor Deleted Successfully";
    }


    // =========================================================
    // 5. INVENTORY
    // =========================================================

    public String createInventory(
            Long vendorId,
            Inventory inventory) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException("Vendor Not Found"));

        inventory.setVendor(vendor);

        inventoryRepository.save(inventory);

        return "Inventory Created Successfully";
    }


    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }


    public Inventory getInventory(Long id) {

        return inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory Not Found"));
    }


    public String updateInventory(
            Long id,
            Inventory inventory) {

        Inventory existing = getInventory(id);

        existing.setItemCode(inventory.getItemCode());
        existing.setItemName(inventory.getItemName());
        existing.setCategory(inventory.getCategory());
        existing.setUnit(inventory.getUnit());
        existing.setQuantity(inventory.getQuantity());
        existing.setMinimumQuantity(
                inventory.getMinimumQuantity());
        existing.setWarehouseLocation(
                inventory.getWarehouseLocation());
        existing.setStatus(inventory.getStatus());

        inventoryRepository.save(existing);

        return "Inventory Updated Successfully";
    }


    public String deleteInventory(Long id) {

        Inventory inventory = getInventory(id);

        inventoryRepository.delete(inventory);

        return "Inventory Deleted Successfully";
    }


    // =========================================================
    // 6. INVENTORY TRANSACTIONS
    // =========================================================

    public String createInventoryTransaction(
            Long inventoryId,
            String employeeId,
            InventoryTransaction transaction) {

        Inventory inventory = inventoryRepository.findById(
                        inventoryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory Not Found"));

        Employee employee = employeeRepository.findById(
                        employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Not Found"));

        transaction.setInventory(inventory);
        transaction.setEmployee(employee);

        /*
         * Update inventory quantity.
         */

        Integer quantity = transaction.getQuantity();

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
        }

        inventoryRepository.save(inventory);

        inventoryTransactionRepository.save(transaction);

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

        existing.setTransactionType(
                transaction.getTransactionType());

        existing.setQuantity(transaction.getQuantity());
        existing.setTransactionDate(
                transaction.getTransactionDate());
        existing.setRemarks(transaction.getRemarks());

        inventoryTransactionRepository.save(existing);

        return "Inventory Transaction Updated Successfully";
    }


    public String deleteInventoryTransaction(Long id) {

        InventoryTransaction transaction =
                getInventoryTransaction(id);

        inventoryTransactionRepository.delete(transaction);

        return "Inventory Transaction Deleted Successfully";
    }


    // =========================================================
    // 7. OFFICE EXPENSES
    // =========================================================

    public String createOfficeExpense(
            Long vendorId,
            String approvedById,
            OfficeExpense expense) {

        Vendor vendor = vendorRepository.findById(Long.valueOf(vendorId))
                .orElseThrow(() ->
                        new RuntimeException("Vendor Not Found"));

        Employee approvedBy = employeeRepository.findById(approvedById)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        expense.setVendor(vendor);
        expense.setApprovedBy(approvedBy);

        officeExpenseRepository.save(expense);

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

        OfficeExpense existing = getOfficeExpense(id);

        existing.setExpenseCategory(
                expense.getExpenseCategory());

        existing.setExpenseDate(
                expense.getExpenseDate());

        existing.setAmount(expense.getAmount());

        existing.setPaymentMode(
                expense.getPaymentMode());

        existing.setRemarks(expense.getRemarks());

        officeExpenseRepository.save(existing);

        return "Office Expense Updated Successfully";
    }


    public String deleteOfficeExpense(Long id) {

        OfficeExpense expense = getOfficeExpense(id);

        officeExpenseRepository.delete(expense);

        return "Office Expense Deleted Successfully";
    }


    // =========================================================
    // 8. COMPANY POLICIES
    // =========================================================

    public String createCompanyPolicy(
            Long companyId,
            CompanyPolicy policy) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        policy.setCompany(company);

        companyPolicyRepository.save(policy);

        return "Company Policy Created Successfully";
    }


    public List<CompanyPolicy> getAllCompanyPolicies() {
        return companyPolicyRepository.findAll();
    }


    public CompanyPolicy getCompanyPolicy(Long id) {

        return companyPolicyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Company Policy Not Found"));
    }


    public String updateCompanyPolicy(
            Long id,
            CompanyPolicy policy) {

        CompanyPolicy existing = getCompanyPolicy(id);

        existing.setPolicyName(policy.getPolicyName());
        existing.setPolicyType(policy.getPolicyType());
        existing.setVersion(policy.getVersion());
        existing.setEffectiveDate(policy.getEffectiveDate());
        existing.setDocumentUrl(policy.getDocumentUrl());
        existing.setStatus(policy.getStatus());

        companyPolicyRepository.save(existing);

        return "Company Policy Updated Successfully";
    }


    public String deleteCompanyPolicy(Long id) {

        CompanyPolicy policy = getCompanyPolicy(id);

        companyPolicyRepository.delete(policy);

        return "Company Policy Deleted Successfully";
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

        announcementRepository.save(announcement);

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

        Announcement existing = getAnnouncement(id);

        existing.setTitle(announcement.getTitle());
        existing.setDescription(
                announcement.getDescription());
        existing.setPublishDate(
                announcement.getPublishDate());
        existing.setExpiryDate(
                announcement.getExpiryDate());
        existing.setPriority(
                announcement.getPriority());

        announcementRepository.save(existing);

        return "Announcement Updated Successfully";
    }


    public String deleteAnnouncement(Long id) {

        Announcement announcement = getAnnouncement(id);

        announcementRepository.delete(announcement);

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

        eventRepository.save(event);

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

        Event existing = getEvent(id);

        existing.setEventName(event.getEventName());
        existing.setEventType(event.getEventType());
        existing.setEventDate(event.getEventDate());
        existing.setStartTime(event.getStartTime());
        existing.setEndTime(event.getEndTime());
        existing.setVenue(event.getVenue());
        existing.setDescription(event.getDescription());

        eventRepository.save(existing);

        return "Event Updated Successfully";
    }


    public String deleteEvent(Long id) {

        Event event = getEvent(id);

        eventRepository.delete(event);

        return "Event Deleted Successfully";
    }
}

