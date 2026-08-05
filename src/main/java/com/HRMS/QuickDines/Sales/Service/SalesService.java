package com.HRMS.QuickDines.Sales.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Sales.model.*;
import com.HRMS.QuickDines.Sales.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final BusServicesRepository busServicesRepository;
    private final SalesTargetsRepository salesTargetsRepository;
    private final SalesReportRepository salesReportRepository;
    private final SalesIncentiveRepository salesIncentiveRepository;

    //=================================
// RESTAURANTS
//=================================

    public String createRestaurant(Restaurant restaurant){

        restaurantsRepository.save(restaurant);
        return "Restaurant Created Successfully";
    }

    public Object getRestaurants(){

        return restaurantsRepository.findAll();
    }

    public Object getRestaurant(Long id){

        return restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));
    }

    public String updateRestaurant(Long id, Restaurant restaurant){

        Restaurant data = restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));

        data.setRestaurantName(restaurant.getRestaurantName());
        data.setOwnerName(restaurant.getOwnerName());
        data.setEmail(restaurant.getEmail());
        data.setMobile(restaurant.getMobile());
        data.setLocation(restaurant.getLocation());
        data.setStatus(restaurant.getStatus());

        restaurantsRepository.save(data);

        return "Restaurant Updated Successfully";
    }

    public String deleteRestaurant(Long id){

        Restaurant data = restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant Not Found"));
        restaurantsRepository.delete(data);

        return "Restaurant Deleted Successfully";
    }


    //=================================
// BUS SERVICES
//=================================

    public String createBusService(BusService busService){

        busServicesRepository.save(busService);

        return "Bus Service Created Successfully";
    }

    public Object getBusServices(){

        return busServicesRepository.findAll();
    }

    public Object getBusService(Long id){

        return busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));
    }

    public String updateBusService(Long id, BusService busService){

        BusService data = busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));

        data.setServiceName(busService.getServiceName());
        data.setOwnerName(busService.getOwnerName());
        data.setMobile(busService.getMobile());
        data.setLocation(busService.getLocation());
        data.setStatus(busService.getStatus());

        busServicesRepository.save(data);

        return "Bus Service Updated Successfully";
    }

    public String deleteBusService(Long id){

        BusService data = busServicesRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus Service Not Found"));
        busServicesRepository.delete(data);
        return "Bus Service Deleted Successfully";
    }


    //=================================
// SALES TARGETS
//=================================

    public String createTarget(String employeeId, SalesTarget salesTarget){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        salesTarget.setEmployee(employee);

        if(salesTarget.getAchievedTarget() == null){
            salesTarget.setAchievedTarget(BigDecimal.ZERO);
        }

        if (salesTarget.getAchievedTarget().compareTo(salesTarget.getMonthlyTarget()) >= 0) {
            salesTarget.setTargetStatus("ACHIEVED");
        } else {
            salesTarget.setTargetStatus("PENDING");
        }

        salesTargetsRepository.save(salesTarget);

        return "Sales Target Created Successfully";
    }

    public Object getTargets(){

        return salesTargetsRepository.findAll();
    }

    public Object getEmployeeTarget(String employeeId){

        return salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));
    }

    public String updateTarget(String employeeId, SalesTarget salesTarget){

        SalesTarget target = salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));

        target.setMonthlyTarget(salesTarget.getMonthlyTarget());
        target.setAchievedTarget(salesTarget.getAchievedTarget());

        if (target.getAchievedTarget().compareTo(target.getMonthlyTarget()) >= 0) {
            target.setTargetStatus("ACHIEVED");
        } else {
            target.setTargetStatus("PENDING");
        }

        salesTargetsRepository.save(target);

        return "Sales Target Updated Successfully";
    }

    public String deleteTarget(String employeeId){

        SalesTarget target = salesTargetsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Sales Target Not Found"));

        salesTargetsRepository.delete(target);

        return "Sales Target Deleted Successfully";
    }


    //=================================
// SALES REPORTS
//=================================

    public String createReport(String employeeId, SalesReport report) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        report.setEmployee(employee);

        salesReportRepository.save(report);

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

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesReport report = (SalesReport) salesReportRepository.findByEmployee(employee);

        if (report == null) {
            throw new RuntimeException("Sales Report Not Found");
        }

        report.setTotalVisits(request.getTotalVisits());
        report.setCompletedDeals(request.getCompletedDeals());
        report.setIncentives(request.getIncentives());
        report.setRemarks(request.getRemarks());

        salesReportRepository.save(report);

        return "Sales Report Updated Successfully";
    }

    public String deleteReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesReport report = (SalesReport) salesReportRepository.findByEmployee(employee);

        if (report == null) {
            throw new RuntimeException("Sales Report Not Found");
        }

        salesReportRepository.delete(report);

        return "Sales Report Deleted Successfully";
    }


    //=================================
// SALES INCENTIVES
//=================================

    public String createIncentive(String employeeId, SalesIncentive incentive){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        incentive.setEmployee(employee);

        salesIncentiveRepository.save(incentive);

        return "Sales Incentive Created Successfully";
    }

    public List<SalesIncentive> getIncentives(){

        return salesIncentiveRepository.findAll();
    }

    public List<SalesIncentive> getEmployeeIncentive(
            String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return Collections.singletonList(salesIncentiveRepository.findByEmployee(employee));
    }

    public String updateIncentive(String employeeId, SalesIncentive request){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalesIncentive incentive = salesIncentiveRepository.findByEmployee(employee);

        if(incentive == null){
            throw new RuntimeException("Sales Incentive Not Found");
        }

        incentive.setIncentiveAmount(request.getIncentiveAmount());
        incentive.setTargetBonus(request.getTargetBonus());
        incentive.setCreditedDate(request.getCreditedDate());

        salesIncentiveRepository.save(incentive);

        return "Sales Incentive Updated Successfully";
    }

    public String deleteIncentive(String employeeId){

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        SalesIncentive incentive = salesIncentiveRepository.findByEmployee(employee);

        if(incentive == null){
            throw new RuntimeException("Sales Incentive Not Found");
        }

        salesIncentiveRepository.delete(incentive);

        return "Sales Incentive Deleted Successfully";
    }

    //=================================
// REPORTS
//=================================

    public List<SalesTarget> completedTargets(){

        return salesTargetsRepository.findByTargetStatus("ACHIEVED");
    }

    public List<SalesTarget> pendingTargets(){

        return salesTargetsRepository
                .findByTargetStatus("PENDING");
    }

    public List<Restaurant> activeRestaurants(){

        return restaurantsRepository.findByStatus("ACTIVE");
    }

    public List<Restaurant> inactiveRestaurants(){

        return restaurantsRepository
                .findByStatus("INACTIVE");
    }

    public List<BusService> activeBusServices(){

        return busServicesRepository.findByStatus("ACTIVE");
    }

    public List<BusService> inactiveBusServices(){

        return busServicesRepository.findByStatus("INACTIVE");
    }


    //=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts(){

        Map<String, Object> counts = new HashMap<>();

        counts.put("totalRestaurants",
                restaurantsRepository.count());

        counts.put("activeRestaurants",
                restaurantsRepository.countByStatus("ACTIVE"));

        counts.put("inactiveRestaurants",
                restaurantsRepository.countByStatus("INACTIVE"));

        counts.put("totalBusServices",
                busServicesRepository.count());

        counts.put("activeBusServices",
                busServicesRepository.countByStatus("ACTIVE"));

        counts.put("inactiveBusServices",
                busServicesRepository.countByStatus("INACTIVE"));

        counts.put("totalSalesTargets",
                salesTargetsRepository.count());

        counts.put("completedTargets",
                salesTargetsRepository.countByTargetStatus("ACHIEVED"));

        counts.put("pendingTargets",
                salesTargetsRepository.countByTargetStatus("PENDING"));

        counts.put("totalSalesReports",
                salesReportRepository.count());

        counts.put("totalIncentives",
                salesIncentiveRepository.count());

        return counts;
    }

}
