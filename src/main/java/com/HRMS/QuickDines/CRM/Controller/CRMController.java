package com.HRMS.QuickDines.CRM.Controller;

import com.HRMS.QuickDines.CRM.Service.CRMService;
import com.HRMS.QuickDines.CRM.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm")
@RequiredArgsConstructor
public class CRMController {

    private final CRMService service;


    // =========================================================
    // CUSTOMERS
    // =========================================================

    @PostMapping("/customer/{employeeId}")
    public ResponseEntity<?> createCustomer(
            @PathVariable String employeeId,
            @RequestBody Customer customer) {

        return ResponseEntity.ok(
                service.createCustomer(employeeId, customer));
    }


    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {

        return ResponseEntity.ok(
                service.getAllCustomers());
    }


    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCustomer(id));
    }


    @PutMapping("/customer/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {

        return ResponseEntity.ok(
                service.updateCustomer(id, customer));
    }


    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCustomer(id));
    }


    // =========================================================
    // LEADS
    // =========================================================

    @PostMapping("/lead/{customerId}/{employeeId}")
    public ResponseEntity<?> createLead(
            @PathVariable Long customerId,
            @PathVariable String employeeId,
            @RequestBody Lead lead) {

        return ResponseEntity.ok(
                service.createLead(
                        customerId,
                        employeeId,
                        lead));
    }


    @GetMapping("/leads")
    public ResponseEntity<?> getAllLeads() {

        return ResponseEntity.ok(
                service.getAllLeads());
    }


    @GetMapping("/lead/{id}")
    public ResponseEntity<?> getLead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLead(id));
    }


    @PutMapping("/lead/{id}")
    public ResponseEntity<?> updateLead(
            @PathVariable Long id,
            @RequestBody Lead lead) {

        return ResponseEntity.ok(
                service.updateLead(id, lead));
    }


    @DeleteMapping("/lead/{id}")
    public ResponseEntity<?> deleteLead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLead(id));
    }


    // =========================================================
    // OPPORTUNITIES
    // =========================================================

    @PostMapping("/opportunity/{leadId}/{employeeId}")
    public ResponseEntity<?> createOpportunity(
            @PathVariable Long leadId,
            @PathVariable String employeeId,
            @RequestBody Opportunity opportunity) {

        return ResponseEntity.ok(
                service.createOpportunity(
                        leadId,
                        employeeId,
                        opportunity));
    }


    @GetMapping("/opportunities")
    public ResponseEntity<?> getAllOpportunities() {

        return ResponseEntity.ok(
                service.getAllOpportunities());
    }


    @GetMapping("/opportunity/{id}")
    public ResponseEntity<?> getOpportunity(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getOpportunity(id));
    }


    @PutMapping("/opportunity/{id}")
    public ResponseEntity<?> updateOpportunity(
            @PathVariable Long id,
            @RequestBody Opportunity opportunity) {

        return ResponseEntity.ok(
                service.updateOpportunity(id, opportunity));
    }


    @DeleteMapping("/opportunity/{id}")
    public ResponseEntity<?> deleteOpportunity(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteOpportunity(id));
    }


    // =========================================================
    // QUOTATIONS
    // =========================================================

    @PostMapping("/quotation/{opportunityId}/{customerId}/{employeeId}")
    public ResponseEntity<?> createQuotation(
            @PathVariable Long opportunityId,
            @PathVariable Long customerId,
            @PathVariable String employeeId,
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                service.createQuotation(
                        opportunityId,
                        customerId,
                        employeeId,
                        quotation));
    }


    @GetMapping("/quotations")
    public ResponseEntity<?> getAllQuotations() {

        return ResponseEntity.ok(
                service.getAllQuotations());
    }


    @GetMapping("/quotation/{id}")
    public ResponseEntity<?> getQuotation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getQuotation(id));
    }


    @PutMapping("/quotation/{id}")
    public ResponseEntity<?> updateQuotation(
            @PathVariable Long id,
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                service.updateQuotation(id, quotation));
    }


    @DeleteMapping("/quotation/{id}")
    public ResponseEntity<?> deleteQuotation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteQuotation(id));
    }


    // =========================================================
    // FOLLOWUPS
    // =========================================================

    @PostMapping("/followup/{customerId}/{leadId}/{employeeId}")
    public ResponseEntity<?> createFollowup(
            @PathVariable Long customerId,
            @PathVariable Long leadId,
            @PathVariable String employeeId,
            @RequestBody Followup followup) {

        return ResponseEntity.ok(
                service.createFollowup(
                        customerId,
                        leadId,
                        employeeId,
                        followup));
    }


    @GetMapping("/followups")
    public ResponseEntity<?> getAllFollowups() {

        return ResponseEntity.ok(
                service.getAllFollowups());
    }


    @GetMapping("/followup/{id}")
    public ResponseEntity<?> getFollowup(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getFollowup(id));
    }


    @PutMapping("/followup/{id}")
    public ResponseEntity<?> updateFollowup(
            @PathVariable Long id,
            @RequestBody Followup followup) {

        return ResponseEntity.ok(
                service.updateFollowup(id, followup));
    }


    @DeleteMapping("/followup/{id}")
    public ResponseEntity<?> deleteFollowup(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteFollowup(id));
    }


    // =========================================================
    // CUSTOMER MEETINGS
    // =========================================================

    @PostMapping("/meeting/{customerId}/{opportunityId}/{employeeId}")
    public ResponseEntity<?> createCustomerMeeting(
            @PathVariable Long customerId,
            @PathVariable Long opportunityId,
            @PathVariable String employeeId,
            @RequestBody CustomerMeeting meeting) {

        return ResponseEntity.ok(
                service.createCustomerMeeting(
                        customerId,
                        opportunityId,
                        employeeId,
                        meeting));
    }


    @GetMapping("/meetings")
    public ResponseEntity<?> getAllCustomerMeetings() {

        return ResponseEntity.ok(
                service.getAllCustomerMeetings());
    }


    @GetMapping("/meeting/{id}")
    public ResponseEntity<?> getCustomerMeeting(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCustomerMeeting(id));
    }


    @PutMapping("/meeting/{id}")
    public ResponseEntity<?> updateCustomerMeeting(
            @PathVariable Long id,
            @RequestBody CustomerMeeting meeting) {

        return ResponseEntity.ok(
                service.updateCustomerMeeting(id, meeting));
    }


    @DeleteMapping("/meeting/{id}")
    public ResponseEntity<?> deleteCustomerMeeting(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCustomerMeeting(id));
    }
}
