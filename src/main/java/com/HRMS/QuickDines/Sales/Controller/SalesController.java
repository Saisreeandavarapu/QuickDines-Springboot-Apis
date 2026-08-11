package com.HRMS.QuickDines.Sales.Controller;

import com.HRMS.QuickDines.Sales.Entity.BusServiceStatus;
import com.HRMS.QuickDines.Sales.Entity.RestaurantStatus;
import com.HRMS.QuickDines.Sales.Service.SalesService;
import com.HRMS.QuickDines.Sales.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService service;

    //=========================================================
    // RESTAURANTS
    //=========================================================

    @PreAuthorize("hasAuthority('RESTAURANT_CREATE')")
    @PostMapping("/restaurant")
    public ResponseEntity<?> createRestaurant(@RequestBody Restaurant restaurant) {

        return ResponseEntity.ok(service.createRestaurant(restaurant));
    }

    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurants() {

        return ResponseEntity.ok(service.getRestaurants());
    }

    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {

        return ResponseEntity.ok(service.getRestaurant(id));
    }

    @PreAuthorize("hasAuthority('RESTAURANT_UPDATE')")
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {

        return ResponseEntity.ok(service.updateRestaurant(id, restaurant));
    }

    @PreAuthorize("hasAuthority('RESTAURANT_DELETE')")
    @DeleteMapping("/restaurant/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteRestaurant(id));
    }


    //=========================================================
    // BUS SERVICES
    //=========================================================

    @PreAuthorize("hasAuthority('BUS_SERVICE_CREATE')")
    @PostMapping("/bus")
    public ResponseEntity<?> createBusService(@RequestBody BusService busService) {

        return ResponseEntity.ok(service.createBusService(busService));
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_READ')")
    @GetMapping("/buses")
    public ResponseEntity<?> getBusServices() {

        return ResponseEntity.ok(service.getBusServices());
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_READ')")
    @GetMapping("/bus/{id}")
    public ResponseEntity<?> getBusService(@PathVariable Long id) {

        return ResponseEntity.ok(service.getBusService(id));
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_UPDATE')")
    @PutMapping("/bus/{id}")
    public ResponseEntity<?> updateBusService(@PathVariable Long id, @RequestBody BusService busService) {

        return ResponseEntity.ok(service.updateBusService(id, busService));
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_DELETE')")
    @DeleteMapping("/bus/{id}")
    public ResponseEntity<?> deleteBusService(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteBusService(id));
    }


    //=========================================================
    // SALES TARGETS
    //=========================================================

    @PreAuthorize("hasAuthority('SALES_TARGET_CREATE')")
    @PostMapping("/target/{employeeId}")
    public ResponseEntity<?> createTarget(@PathVariable String employeeId, @RequestBody SalesTarget salesTarget) {

        return ResponseEntity.ok(service.createTarget(employeeId, salesTarget));
    }

    @PreAuthorize("hasAuthority('SALES_TARGET_READ')")
    @GetMapping("/targets")
    public ResponseEntity<?> getTargets() {

        return ResponseEntity.ok(service.getTargets());
    }

    @PreAuthorize("hasAuthority('SALES_TARGET_READ')")
    @GetMapping("/target/{employeeId}")
    public ResponseEntity<?> getEmployeeTarget(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getEmployeeTarget(employeeId));
    }

    @PreAuthorize("hasAuthority('SALES_TARGET_UPDATE')")
    @PutMapping("/target/{employeeId}")
    public ResponseEntity<?> updateTarget(@PathVariable String employeeId, @RequestBody SalesTarget salesTarget) {

        return ResponseEntity.ok(service.updateTarget(employeeId, salesTarget));
    }

    @PreAuthorize("hasAuthority('SALES_TARGET_DELETE')")
    @DeleteMapping("/target/{employeeId}")
    public ResponseEntity<?> deleteTarget(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.deleteTarget(employeeId));
    }


    //=========================================================
    // SALES REPORTS
    //=========================================================

    @PreAuthorize("hasAuthority('SALES_REPORT_CREATE')")
    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> createReport(@PathVariable String employeeId, @RequestBody SalesReport report) {

        return ResponseEntity.ok(service.createReport(employeeId, report));
    }

    @PreAuthorize("hasAuthority('SALES_REPORT_READ')")
    @GetMapping("/reports")
    public ResponseEntity<?> getReports() {

        return ResponseEntity.ok(service.getReports());
    }

    @PreAuthorize("hasAuthority('SALES_REPORT_READ')")
    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getEmployeeReport(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getEmployeeReport(employeeId));
    }

    @PreAuthorize("hasAuthority('SALES_REPORT_UPDATE')")
    @PutMapping("/report/{employeeId}")
    public ResponseEntity<?> updateReport(@PathVariable String employeeId, @RequestBody SalesReport report) {

        return ResponseEntity.ok(service.updateReport(employeeId, report));
    }

    @PreAuthorize("hasAuthority('SALES_REPORT_DELETE')")
    @DeleteMapping("/report/{employeeId}")
    public ResponseEntity<?> deleteReport(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.deleteReport(employeeId));
    }


    //=========================================================
    // SALES INCENTIVES
    //=========================================================

    @PreAuthorize("hasAuthority('SALES_INCENTIVE_CREATE')")
    @PostMapping("/incentive/{employeeId}")
    public ResponseEntity<?> createIncentive(@PathVariable String employeeId, @RequestBody SalesIncentive incentive) {

        return ResponseEntity.ok(service.createIncentive(employeeId, incentive));
    }

    @PreAuthorize("hasAuthority('SALES_INCENTIVE_READ')")
    @GetMapping("/incentives")
    public ResponseEntity<?> getIncentives() {

        return ResponseEntity.ok(service.getIncentives());
    }

    @PreAuthorize("hasAuthority('SALES_INCENTIVE_READ')")
    @GetMapping("/incentive/{employeeId}")
    public ResponseEntity<?> getEmployeeIncentive(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getEmployeeIncentive(employeeId));
    }

    @PreAuthorize("hasAuthority('SALES_INCENTIVE_UPDATE')")
    @PutMapping("/incentive/{employeeId}")
    public ResponseEntity<?> updateIncentive(@PathVariable String employeeId, @RequestBody SalesIncentive incentive) {

        return ResponseEntity.ok(service.updateIncentive(employeeId, incentive));
    }

    @PreAuthorize("hasAuthority('SALES_INCENTIVE_DELETE')")
    @DeleteMapping("/incentive/{employeeId}")
    public ResponseEntity<?> deleteIncentive(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.deleteIncentive(employeeId));
    }


    //=========================================================
    // SALES REPORTS / STATUS
    //=========================================================

    @PreAuthorize("hasAuthority('SALES_TARGET_REPORT_READ')")
    @GetMapping("/completed-targets")
    public ResponseEntity<?> completedTargets() {

        return ResponseEntity.ok(service.completedTargets());
    }

    @PreAuthorize("hasAuthority('SALES_TARGET_REPORT_READ')")
    @GetMapping("/pending-targets")
    public ResponseEntity<?> pendingTargets() {

        return ResponseEntity.ok(service.pendingTargets());
    }

    @PreAuthorize("hasAuthority('RESTAURANT_STATUS_READ')")
    @GetMapping("/restaurants/active")
    public ResponseEntity<?> activeRestaurants() {

        return ResponseEntity.ok(service.activeRestaurants());
    }

    @PreAuthorize("hasAuthority('RESTAURANT_STATUS_READ')")
    @GetMapping("/restaurants/inactive")
    public ResponseEntity<?> inactiveRestaurants() {

        return ResponseEntity.ok(service.inactiveRestaurants());
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_STATUS_READ')")
    @GetMapping("/bus/active")
    public ResponseEntity<?> activeBusServices() {

        return ResponseEntity.ok(service.activeBusServices());
    }

    @PreAuthorize("hasAuthority('BUS_SERVICE_STATUS_READ')")
    @GetMapping("/bus/inactive")
    public ResponseEntity<?> inactiveBusServices() {

        return ResponseEntity.ok(service.inactiveBusServices());
    }


    //=========================================================
    // DASHBOARD COUNTS
    //=========================================================

    @PreAuthorize("hasAuthority('SALES_DASHBOARD_READ')")
    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }
    // =========================================================
// BUS SERVICE STATUS FILTER
// =========================================================

    @GetMapping("/bus-services/filter/status")
    @PreAuthorize("hasAuthority('BUS_SERVICE_READ')")
    public ResponseEntity<?> getBusServicesByStatus(@RequestParam BusServiceStatus status) {

        return ResponseEntity.ok(service.getBusServicesByStatus(status));
    }
    // =========================================================
// RESTAURANT STATUS FILTER
// =========================================================

    @GetMapping("/restaurants/filter/status")
    @PreAuthorize("hasAuthority('RESTAURANT_READ')")
    public ResponseEntity<?> getRestaurantsByStatus(@RequestParam RestaurantStatus status) {

        return ResponseEntity.ok(service.getRestaurantsByStatus(status));
    }

    @PostMapping("/import-excel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(service.importExcel(file));
    }

    @PostMapping(value = "/restaurants/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('RESTAURANT_CREATE')")
    public ResponseEntity<?> importRestaurants(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(service.importRestaurantsFromExcel(file));
    }
}
