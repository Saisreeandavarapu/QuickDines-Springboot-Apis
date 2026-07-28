package com.HRMS.QuickDines.Sales.Controller;

import com.HRMS.QuickDines.Sales.Service.SalesService;
import com.HRMS.QuickDines.Sales.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService service;

    //=================================
// RESTAURANTS
//=================================

    @PostMapping("/restaurant")
    public ResponseEntity<?> createRestaurant(
            @RequestBody Restaurant restaurant){

        return ResponseEntity.ok(service.createRestaurant(restaurant));
    }

    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurants(){

        return ResponseEntity.ok(service.getRestaurants());
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getRestaurant(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getRestaurant(id));
    }

    @PutMapping("/restaurant/{id}")
    public ResponseEntity<?> updateRestaurant(
            @PathVariable Long id,
            @RequestBody Restaurant restaurant){

        return ResponseEntity.ok(service.updateRestaurant(id, restaurant));
    }

    @DeleteMapping("/restaurant/{id}")
    public ResponseEntity<?> deleteRestaurant(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteRestaurant(id));
    }


    //=================================
// BUS SERVICES
//=================================

    @PostMapping("/bus")
    public ResponseEntity<?> createBusService(
            @RequestBody BusService busService){

        return ResponseEntity.ok(service.createBusService(busService));
    }

    @GetMapping("/buses")
    public ResponseEntity<?> getBusServices(){

        return ResponseEntity.ok(service.getBusServices());
    }

    @GetMapping("/bus/{id}")
    public ResponseEntity<?> getBusService(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getBusService(id));
    }

    @PutMapping("/bus/{id}")
    public ResponseEntity<?> updateBusService(
            @PathVariable Long id,
            @RequestBody BusService busService){

        return ResponseEntity.ok(service.updateBusService(id, busService));
    }

    @DeleteMapping("/bus/{id}")
    public ResponseEntity<?> deleteBusService(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteBusService(id));
    }


    //=================================
// SALES TARGETS
//=================================

    @PostMapping("/target/{employeeId}")
    public ResponseEntity<?> createTarget(
            @PathVariable String employeeId,
            @RequestBody SalesTarget salesTarget){

        return ResponseEntity.ok(service.createTarget(employeeId, salesTarget));
    }

    @GetMapping("/targets")
    public ResponseEntity<?> getTargets(){

        return ResponseEntity.ok(service.getTargets());
    }

    @GetMapping("/target/{employeeId}")
    public ResponseEntity<?> getEmployeeTarget(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getEmployeeTarget(employeeId));
    }

    @PutMapping("/target/{employeeId}")
    public ResponseEntity<?> updateTarget(
            @PathVariable String employeeId,
            @RequestBody SalesTarget salesTarget){

        return ResponseEntity.ok(service.updateTarget(employeeId, salesTarget));
    }

    @DeleteMapping("/target/{employeeId}")
    public ResponseEntity<?> deleteTarget(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteTarget(employeeId));
    }


    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> createReport(
            @PathVariable String employeeId,
            @RequestBody SalesReport report) {

        return ResponseEntity.ok(service.createReport(employeeId, report));
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports() {

        return ResponseEntity.ok(service.getReports());
    }

    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getEmployeeReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getEmployeeReport(employeeId));
    }

    @PutMapping("/report/{employeeId}")
    public ResponseEntity<?> updateReport(
            @PathVariable String employeeId,
            @RequestBody SalesReport report) {

        return ResponseEntity.ok(service.updateReport(employeeId, report));
    }

    @DeleteMapping("/report/{employeeId}")
    public ResponseEntity<?> deleteReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.deleteReport(employeeId));
    }

    //=================================
// SALES INCENTIVES
//=================================

    @PostMapping("/incentive/{employeeId}")
    public ResponseEntity<?> createIncentive(
            @PathVariable String employeeId,
            @RequestBody SalesIncentive incentive){

        return ResponseEntity.ok(service.createIncentive(employeeId, incentive));
    }

    @GetMapping("/incentives")
    public ResponseEntity<?> getIncentives(){

        return ResponseEntity.ok(service.getIncentives());
    }

    @GetMapping("/incentive/{employeeId}")
    public ResponseEntity<?> getEmployeeIncentive(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getEmployeeIncentive(employeeId));
    }

    @PutMapping("/incentive/{employeeId}")
    public ResponseEntity<?> updateIncentive(
            @PathVariable String employeeId,
            @RequestBody SalesIncentive incentive){

        return ResponseEntity.ok(service.updateIncentive(employeeId, incentive));
    }

    @DeleteMapping("/incentive/{employeeId}")
    public ResponseEntity<?> deleteIncentive(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteIncentive(employeeId));
    }


    //=================================
// REPORTS
//=================================

    @GetMapping("/completed-targets")
    public ResponseEntity<?> completedTargets(){

        return ResponseEntity.ok(service.completedTargets());
    }

    @GetMapping("/pending-targets")
    public ResponseEntity<?> pendingTargets(){

        return ResponseEntity.ok(service.pendingTargets());
    }

    @GetMapping("/restaurants/active")
    public ResponseEntity<?> activeRestaurants(){

        return ResponseEntity.ok(service.activeRestaurants());
    }

    @GetMapping("/restaurants/inactive")
    public ResponseEntity<?> inactiveRestaurants(){

        return ResponseEntity.ok(service.inactiveRestaurants());
    }

    @GetMapping("/bus/active")
    public ResponseEntity<?> activeBusServices(){

        return ResponseEntity.ok(service.activeBusServices());
    }

    @GetMapping("/bus/inactive")
    public ResponseEntity<?> inactiveBusServices(){

        return ResponseEntity.ok(service.inactiveBusServices());
    }


    //=================================
// DASHBOARD COUNTS
//=================================

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts(){

        return ResponseEntity.ok(service.getCounts());
    }

}
