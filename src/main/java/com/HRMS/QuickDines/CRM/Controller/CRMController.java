package com.HRMS.QuickDines.CRM.Controller;

import com.HRMS.QuickDines.CRM.Service.CRMService;
import com.HRMS.QuickDines.CRM.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    public ResponseEntity<?> createCustomer(
            @PathVariable String employeeId,
            @RequestBody Customer customer) {

        return ResponseEntity.ok(
                service.createCustomer(employeeId, customer));
    }


    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<?> getAllCustomers() {

        return ResponseEntity.ok(
                service.getAllCustomers());
    }


    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<?> getCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCustomer(id));
    }


    @PutMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {

        return ResponseEntity.ok(
                service.updateCustomer(id, customer));
    }


    @DeleteMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCustomer(id));
    }


    // =========================================================
    // LEADS
    // =========================================================

    @PostMapping("/lead/{customerId}/{employeeId}")
    @PreAuthorize("hasAuthority('LEAD_CREATE')")
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
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    public ResponseEntity<?> getAllLeads() {

        return ResponseEntity.ok(
                service.getAllLeads());
    }


    @GetMapping("/lead/{id}")
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    public ResponseEntity<?> getLead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLead(id));
    }


    @PutMapping("/lead/{id}")
    @PreAuthorize("hasAuthority('LEAD_UPDATE')")
    public ResponseEntity<?> updateLead(
            @PathVariable Long id,
            @RequestBody Lead lead) {

        return ResponseEntity.ok(
                service.updateLead(id, lead));
    }


    @DeleteMapping("/lead/{id}")
    @PreAuthorize("hasAuthority('LEAD_DELETE')")
    public ResponseEntity<?> deleteLead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLead(id));
    }


    // =========================================================
    // OPPORTUNITIES
    // =========================================================

    @PostMapping("/opportunity/{leadId}/{employeeId}")
    @PreAuthorize("hasAuthority('OPPORTUNITY_CREATE')")
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
    @PreAuthorize("hasAuthority('OPPORTUNITY_VIEW')")
    public ResponseEntity<?> getAllOpportunities() {

        return ResponseEntity.ok(
                service.getAllOpportunities());
    }


    @GetMapping("/opportunity/{id}")
    @PreAuthorize("hasAuthority('OPPORTUNITY_VIEW')")
    public ResponseEntity<?> getOpportunity(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getOpportunity(id));
    }


    @PutMapping("/opportunity/{id}")
    @PreAuthorize("hasAuthority('OPPORTUNITY_UPDATE')")
    public ResponseEntity<?> updateOpportunity(
            @PathVariable Long id,
            @RequestBody Opportunity opportunity) {

        return ResponseEntity.ok(
                service.updateOpportunity(id, opportunity));
    }


    @DeleteMapping("/opportunity/{id}")
    @PreAuthorize("hasAuthority('OPPORTUNITY_DELETE')")
    public ResponseEntity<?> deleteOpportunity(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteOpportunity(id));
    }


    // =========================================================
    // QUOTATIONS
    // =========================================================

    @PostMapping("/quotation/{opportunityId}/{customerId}/{employeeId}")
    @PreAuthorize("hasAuthority('QUOTATION_CREATE')")
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
    @PreAuthorize("hasAuthority('QUOTATION_VIEW')")
    public ResponseEntity<?> getAllQuotations() {

        return ResponseEntity.ok(
                service.getAllQuotations());
    }


    @GetMapping("/quotation/{id}")
    @PreAuthorize("hasAuthority('QUOTATION_VIEW')")
    public ResponseEntity<?> getQuotation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getQuotation(id));
    }


    @PutMapping("/quotation/{id}")
    @PreAuthorize("hasAuthority('QUOTATION_UPDATE')")
    public ResponseEntity<?> updateQuotation(
            @PathVariable Long id,
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                service.updateQuotation(id, quotation));
    }


    @DeleteMapping("/quotation/{id}")
    @PreAuthorize("hasAuthority('QUOTATION_DELETE')")
    public ResponseEntity<?> deleteQuotation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteQuotation(id));
    }


    // =========================================================
    // FOLLOWUPS
    // =========================================================

    @PostMapping("/followup/{customerId}/{leadId}/{employeeId}")
    @PreAuthorize("hasAuthority('FOLLOWUP_CREATE')")
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
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<?> getAllFollowups() {

        return ResponseEntity.ok(
                service.getAllFollowups());
    }


    @GetMapping("/followup/{id}")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<?> getFollowup(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getFollowup(id));
    }


    @PutMapping("/followup/{id}")
    @PreAuthorize("hasAuthority('FOLLOWUP_UPDATE')")
    public ResponseEntity<?> updateFollowup(
            @PathVariable Long id,
            @RequestBody Followup followup) {

        return ResponseEntity.ok(
                service.updateFollowup(id, followup));
    }


    @DeleteMapping("/followup/{id}")
    @PreAuthorize("hasAuthority('FOLLOWUP_DELETE')")
    public ResponseEntity<?> deleteFollowup(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteFollowup(id));
    }


    // =========================================================
    // CUSTOMER MEETINGS
    // =========================================================

    @PostMapping("/meeting/{customerId}/{opportunityId}/{employeeId}")
    @PreAuthorize("hasAuthority('MEETING_CREATE')")
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
    @PreAuthorize("hasAuthority('MEETING_VIEW')")
    public ResponseEntity<?> getAllCustomerMeetings() {

        return ResponseEntity.ok(
                service.getAllCustomerMeetings());
    }


    @GetMapping("/meeting/{id}")
    @PreAuthorize("hasAuthority('MEETING_VIEW')")
    public ResponseEntity<?> getCustomerMeeting(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCustomerMeeting(id));
    }


    @PutMapping("/meeting/{id}")
    @PreAuthorize("hasAuthority('MEETING_UPDATE')")
    public ResponseEntity<?> updateCustomerMeeting(
            @PathVariable Long id,
            @RequestBody CustomerMeeting meeting) {

        return ResponseEntity.ok(
                service.updateCustomerMeeting(id, meeting));
    }


    @DeleteMapping("/meeting/{id}")
    @PreAuthorize("hasAuthority('MEETING_DELETE')")
    public ResponseEntity<?> deleteCustomerMeeting(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCustomerMeeting(id));
    }
}