package com.HRMS.QuickDines.Sales.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Sales.Entity.BusServiceStatus;
import com.HRMS.QuickDines.Sales.Entity.RestaurantStatus;
import com.HRMS.QuickDines.Sales.model.*;
import com.HRMS.QuickDines.Sales.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final BusServicesRepository busServicesRepository;
    private final SalesTargetsRepository salesTargetsRepository;
    private final SalesReportRepository salesReportRepository;
    private final SalesIncentiveRepository salesIncentiveRepository;
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

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


//=================================
// RESTAURANTS
//=================================

    public String createRestaurant(Restaurant restaurant) {

        restaurantsRepository.save(restaurant);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("RESTAURANT", String.valueOf(restaurant.getId()), performedBy, restaurant.getId().toString(), "Restaurant created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_RESTAURANT", "SALES", "Restaurant created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Restaurant created successfully");

        return "Restaurant Created Successfully";
    }


    public Object getRestaurants() {

        return restaurantsRepository.findAll();
    }


    public Object getRestaurant(Long id) {

        return restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));
    }


    public String updateRestaurant(Long id, Restaurant restaurant) {

        Restaurant data = restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));

        String oldValue = convertToJson(data);

        data.setRestaurantName(restaurant.getRestaurantName());

        data.setOwnerName(restaurant.getOwnerName());

        data.setEmail(restaurant.getEmail());

        data.setMobile(restaurant.getMobile());

        data.setLocation(restaurant.getLocation());

        data.setStatus(restaurant.getStatus());

        restaurantsRepository.save(data);

        String newValue = convertToJson(data);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("RESTAURANT", String.valueOf(id), performedBy, restaurant.getId().toString(), "Restaurant updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_RESTAURANT", "SALES", "Restaurant updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Restaurant updated successfully");

        return "Restaurant Updated Successfully";
    }


    public String deleteRestaurant(Long id) {

        Restaurant data = restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));

        String deletedValue = convertToJson(data);

        String performedBy = getLoggedInEmployeeId();

        restaurantsRepository.delete(data);

        auditLogsService.createAuditLog("RESTAURANT", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, data.getId().toString(), "Restaurant deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_RESTAURANT", "SALES", "Restaurant deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Restaurant deleted successfully");

        return "Restaurant Deleted Successfully";
    }


//=================================
// BUS SERVICES
//=================================

    public String createBusService(BusService busService) {

        busServicesRepository.save(busService);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("BUS_SERVICE", String.valueOf(busService.getId()), performedBy, busService.getId().toString(), "Bus Service created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_BUS_SERVICE", "SALES", "Bus Service created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Bus Service created successfully");

        return "Bus Service Created Successfully";
    }


    public Object getBusServices() {

        return busServicesRepository.findAll();
    }


    public Object getBusService(Long id) {

        return busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));
    }


    public String updateBusService(Long id, BusService busService) {

        BusService data = busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));

        String oldValue = convertToJson(data);

        data.setServiceName(busService.getServiceName());

        data.setOwnerName(busService.getOwnerName());

        data.setMobile(busService.getMobile());

        data.setLocation(busService.getLocation());

        data.setStatus(busService.getStatus());

        busServicesRepository.save(data);

        String newValue = convertToJson(data);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("BUS_SERVICE", String.valueOf(id), performedBy, busService.getId().toString(), "Bus Service updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_BUS_SERVICE", "SALES", "Bus Service updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Bus Service updated successfully");

        return "Bus Service Updated Successfully";
    }


    public String deleteBusService(Long id) {

        BusService data = busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));

        String deletedValue = convertToJson(data);

        String performedBy = getLoggedInEmployeeId();

        busServicesRepository.delete(data);

        auditLogsService.createAuditLog("BUS_SERVICE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, data.getId().toString(), "Bus Service deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_BUS_SERVICE", "SALES", "Bus Service deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Bus Service deleted successfully");

        return "Bus Service Deleted Successfully";
    }


//=================================
// SALES TARGETS
//=================================

    public String createTarget(String employeeId, SalesTarget salesTarget) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        salesTarget.setEmployee(employee);

        if (salesTarget.getAchievedTarget() == null) {
            salesTarget.setAchievedTarget(BigDecimal.ZERO);
        }

        if (salesTarget.getMonthlyTarget() == null) {
            throw new RuntimeException("Monthly Target is Required");
        }

        if (salesTarget.getAchievedTarget().compareTo(salesTarget.getMonthlyTarget()) >= 0) {

            salesTarget.setTargetStatus("ACHIEVED");

        } else {

            salesTarget.setTargetStatus("PENDING");
        }

        salesTargetsRepository.save(salesTarget);

        //=================================
        // AUDIT LOG - CREATE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("SALES_TARGET", String.valueOf(salesTarget.getId()), performedBy, employeeId, "Sales Target created successfully for employee: " + employeeId);

        auditLogsService.logActivity(performedBy, "CREATE_SALES_TARGET", "SALES", "Sales Target created successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Target created successfully for employee: " + employeeId);

        return "Sales Target Created Successfully";
    }


    public Object getTargets() {

        return salesTargetsRepository.findAll();
    }


    public Object getEmployeeTarget(String employeeId) {

        return salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));
    }


    public String updateTarget(String employeeId, SalesTarget salesTarget) {

        SalesTarget target = salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));

        // Store old value before update
        String oldValue = convertToJson(target);

        target.setMonthlyTarget(salesTarget.getMonthlyTarget());
        target.setAchievedTarget(salesTarget.getAchievedTarget());

        if (target.getAchievedTarget() == null) {
            target.setAchievedTarget(BigDecimal.ZERO);
        }

        if (target.getMonthlyTarget() == null) {
            throw new RuntimeException("Monthly Target is Required");
        }

        if (target.getAchievedTarget().compareTo(target.getMonthlyTarget()) >= 0) {

            target.setTargetStatus("ACHIEVED");

        } else {

            target.setTargetStatus("PENDING");
        }

        salesTargetsRepository.save(target);

        // Store new value after update
        String newValue = convertToJson(target);

        //=================================
        // AUDIT LOG - UPDATE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("SALES_TARGET", String.valueOf(target.getId()), performedBy, employeeId, "Sales Target updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_SALES_TARGET", "SALES", "Sales Target updated successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Target updated successfully for employee: " + employeeId);

        return "Sales Target Updated Successfully";
    }


    public String deleteTarget(String employeeId) {

        SalesTarget target = salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));

        // Store old value before delete
        String deletedValue = convertToJson(target);

        salesTargetsRepository.delete(target);

        //=================================
        // AUDIT LOG - DELETE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog("SALES_TARGET", String.valueOf(target.getId()), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Sales Target deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_SALES_TARGET", "SALES", "Sales Target deleted successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Target deleted successfully for employee: " + employeeId);

        return "Sales Target Deleted Successfully";
    }


//=================================
// SALES REPORTS
//=================================

    public String createReport(String employeeId, SalesReport report) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        report.setEmployee(employee);

        salesReportRepository.save(report);

        //=================================
        // AUDIT LOG - CREATE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("SALES_REPORT", String.valueOf(report.getId()), performedBy, employeeId, "Sales Report created successfully for employee: " + employeeId);

        auditLogsService.logActivity(performedBy, "CREATE_SALES_REPORT", "SALES", "Sales Report created successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Report created successfully for employee: " + employeeId);

        return "Sales Report Created Successfully";
    }


    public List<SalesReport> getReports() {

        return salesReportRepository.findAll();
    }


    public List<SalesReport> getEmployeeReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return salesReportRepository.findByEmployee(employee);
    }


    public String updateReport(String employeeId, SalesReport request) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        List<SalesReport> reports = salesReportRepository.findByEmployee(employee);

        if (reports == null || reports.isEmpty()) {
            throw new RuntimeException("Sales Report Not Found");
        }

        // Assuming one report per employee
        SalesReport report = reports.get(0);

        // Store old value before update
        String oldValue = convertToJson(report);

        report.setTotalVisits(request.getTotalVisits());
        report.setCompletedDeals(request.getCompletedDeals());
        report.setIncentives(request.getIncentives());
        report.setRemarks(request.getRemarks());

        salesReportRepository.save(report);

        // Store new value after update
        String newValue = convertToJson(report);

        //=================================
        // AUDIT LOG - UPDATE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("SALES_REPORT", String.valueOf(report.getId()), performedBy, employeeId, "Sales Report updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_SALES_REPORT", "SALES", "Sales Report updated successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Report updated successfully for employee: " + employeeId);

        return "Sales Report Updated Successfully";
    }


    public String deleteReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        List<SalesReport> reports = salesReportRepository.findByEmployee(employee);

        if (reports == null || reports.isEmpty()) {
            throw new RuntimeException("Sales Report Not Found");
        }

        // Assuming one report per employee
        SalesReport report = reports.get(0);

        // Store old value before delete
        String deletedValue = convertToJson(report);

        salesReportRepository.delete(report);

        //=================================
        // AUDIT LOG - DELETE
        //=================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog("SALES_REPORT", String.valueOf(report.getId()), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Sales Report deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_SALES_REPORT", "SALES", "Sales Report deleted successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Report deleted successfully for employee: " + employeeId);

        return "Sales Report Deleted Successfully";
    }


//=================================
// SALES INCENTIVES
//=================================

    public String createIncentive(String employeeId, SalesIncentive incentive) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        incentive.setEmployee(employee);

        salesIncentiveRepository.save(incentive);

        // ================================
        // AUDIT LOG - CREATE
        // ================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("SALES", String.valueOf(incentive.getId()), performedBy, employeeId, "Sales Incentive created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_SALES_INCENTIVE", "SALES", "Sales Incentive created successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Incentive created successfully for employee: " + employeeId);

        return "Sales Incentive Created Successfully";
    }


    public List<SalesIncentive> getIncentives() {

        return salesIncentiveRepository.findAll();
    }


    public List<SalesIncentive> getEmployeeIncentive(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesIncentive incentive = salesIncentiveRepository.findByEmployee(employee);

        if (incentive == null) {
            throw new RuntimeException("Sales Incentive Not Found");
        }

        return Collections.singletonList(incentive);
    }


    public String updateIncentive(String employeeId, SalesIncentive request) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesIncentive incentive = salesIncentiveRepository.findByEmployee(employee);

        if (incentive == null) {
            throw new RuntimeException("Sales Incentive Not Found");
        }

        // Keep old value before updating
        String oldValue = convertToJson(incentive);

        incentive.setIncentiveAmount(request.getIncentiveAmount());

        incentive.setTargetBonus(request.getTargetBonus());

        incentive.setCreditedDate(request.getCreditedDate());

        salesIncentiveRepository.save(incentive);

        // Convert updated object to JSON
        String newValue = convertToJson(incentive);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - UPDATE
        // ================================

        auditLogsService.logUpdate("SALES", String.valueOf(incentive.getId()), performedBy, employeeId, null, oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_SALES_INCENTIVE", "SALES", "Sales Incentive updated successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Incentive updated successfully for employee: " + employeeId);

        return "Sales Incentive Updated Successfully";
    }


    public String deleteIncentive(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesIncentive incentive = salesIncentiveRepository.findByEmployee(employee);

        if (incentive == null) {
            throw new RuntimeException("Sales Incentive Not Found");
        }

        // Keep deleted data for audit
        String deletedValue = convertToJson(incentive);

        Long incentiveId = incentive.getId();

        salesIncentiveRepository.delete(incentive);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - DELETE
        // ================================

        auditLogsService.createAuditLog("SALES", String.valueOf(incentiveId), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Sales Incentive deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_SALES_INCENTIVE", "SALES", "Sales Incentive deleted successfully for employee: " + employeeId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("SALES", "SalesService", "Sales Incentive deleted successfully for employee: " + employeeId);

        return "Sales Incentive Deleted Successfully";
    }


    //=================================
// REPORTS
//=================================

    public List<SalesTarget> completedTargets() {

        return salesTargetsRepository.findByTargetStatus("ACHIEVED");
    }

    public List<SalesTarget> pendingTargets() {

        return salesTargetsRepository.findByTargetStatus("PENDING");
    }

    public List<Restaurant> activeRestaurants() {

        return restaurantsRepository.findByStatus("ACTIVE");
    }

    public List<Restaurant> inactiveRestaurants() {

        return restaurantsRepository.findByStatus("INACTIVE");
    }

    public List<BusService> activeBusServices() {

        return busServicesRepository.findByStatus("ACTIVE");
    }

    public List<BusService> inactiveBusServices() {

        return busServicesRepository.findByStatus("INACTIVE");
    }


    //=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts() {

        Map<String, Object> counts = new HashMap<>();

        counts.put("totalRestaurants", restaurantsRepository.count());

        counts.put("activeRestaurants", restaurantsRepository.countByStatus("ACTIVE"));

        counts.put("inactiveRestaurants", restaurantsRepository.countByStatus("INACTIVE"));

        counts.put("totalBusServices", busServicesRepository.count());

        counts.put("activeBusServices", busServicesRepository.countByStatus("ACTIVE"));

        counts.put("inactiveBusServices", busServicesRepository.countByStatus("INACTIVE"));

        counts.put("totalSalesTargets", salesTargetsRepository.count());

        counts.put("completedTargets", salesTargetsRepository.countByTargetStatus("ACHIEVED"));

        counts.put("pendingTargets", salesTargetsRepository.countByTargetStatus("PENDING"));

        counts.put("totalSalesReports", salesReportRepository.count());

        counts.put("totalIncentives", salesIncentiveRepository.count());

        return counts;
    }
// =========================================================
// BUS SERVICE STATUS FILTER
// =========================================================

    public List<BusService> getBusServicesByStatus(BusServiceStatus status) {

        if (status == null) {
            throw new RuntimeException("Bus Service status is required");
        }

        return busServicesRepository.findByStatus(status);
    }
    // =========================================================
// RESTAURANT STATUS FILTER
// =========================================================

    public List<Restaurant> getRestaurantsByStatus(RestaurantStatus status) {

        if (status == null) {
            throw new RuntimeException("Restaurant status is required");
        }

        return restaurantsRepository.findByStatus(status);
    }

    public String importExcel(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please upload an Excel file");
        }

        List<BusService> busServices = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                BusService busService = new BusService();

                busService.setServiceName(getCellValue(row.getCell(0)));

                busService.setOwnerName(getCellValue(row.getCell(1)));

                busService.setMobile(getCellValue(row.getCell(2)));

                busService.setLocation(getCellValue(row.getCell(3)));

                String status = getCellValue(row.getCell(4));

                if (status == null || status.isBlank()) {

                    busService.setStatus(BusServiceStatus.ACTIVE);

                } else {

                    try {

                        busService.setStatus(BusServiceStatus.valueOf(status.trim().toUpperCase()));

                    } catch (IllegalArgumentException e) {

                        throw new RuntimeException("Invalid status at Excel row " + (i + 1) + ": " + status);
                    }
                }

                busServices.add(busService);
            }

            busServicesRepository.saveAll(busServices);

            return busServices.size() + " Bus Services Imported Successfully";

        } catch (Exception e) {

            throw new RuntimeException("Excel import failed: " + e.getMessage(), e);
        }
    }


    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell).trim();
    }

    public String importRestaurantsFromExcel(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Excel file is empty");
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<Restaurant> restaurants = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                // Skip completely empty rows
                if (row.getCell(0) == null || row.getCell(0).toString().isBlank()) {
                    continue;
                }

                Restaurant restaurant = new Restaurant();

                // Column 0 - Restaurant Name
                restaurant.setRestaurantName(getCellValue(row.getCell(0)));

                // Column 1 - Owner Name
                restaurant.setOwnerName(getCellValue(row.getCell(1)));

                // Column 2 - Email
                restaurant.setEmail(getCellValue(row.getCell(2)));

                // Column 3 - Mobile
                restaurant.setMobile(getCellValue(row.getCell(3)));

                // Column 4 - Location
                restaurant.setLocation(getCellValue(row.getCell(4)));

                // Column 5 - Status
                String status = getCellValue(row.getCell(5));

                if (status == null || status.isBlank()) {

                    restaurant.setStatus(RestaurantStatus.ACTIVE);

                } else {

                    try {

                        restaurant.setStatus(RestaurantStatus.valueOf(status.trim().toUpperCase()));

                    } catch (IllegalArgumentException e) {

                        throw new RuntimeException("Invalid restaurant status at row " + (i + 1) + ": " + status);
                    }
                }

                restaurants.add(restaurant);
            }

            restaurantsRepository.saveAll(restaurants);

            return restaurants.size() + " Restaurants Imported Successfully";

        } catch (IOException e) {

            throw new RuntimeException("Failed to read Excel file", e);

        } catch (Exception e) {

            throw new RuntimeException("Failed to import restaurants: " + e.getMessage(), e);
        }
    }

}
