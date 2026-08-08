package com.HRMS.QuickDines.CRM.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.CRM.model.*;
import com.HRMS.QuickDines.CRM.repo.*;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CRMService {

    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;
    private final OpportunityRepository opportunityRepository;
    private final QuotationRepository quotationRepository;
    private final FollowupRepository followupRepository;
    private final CustomerMeetingRepository customerMeetingRepository;

    private final EmployeeRepository employeeRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper;
   

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
    String performedBy = getLoggedInEmployeeId();
// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            return objectMapper.writeValueAsString(object);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error converting object to JSON",
                    e
            );
        }
    }




    // =========================================================
    // CUSTOMERS
    // =========================================================

  

    public String createCustomer(
            String employeeId,
            Customer customer) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        customer.setAssignedSalesEmployee(employee);

        customerRepository.save(customer);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(customer);

        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                performedBy,
                "CREATE_CUSTOMER",
                "CRM",
                "Customer created successfully. Customer ID: "
                        + customer.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(
                "CRM",
                customer.getId().toString(),
                AuditActionType.CREATE,
                performedBy,
                performedBy,
                "Customer created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer created successfully. Customer ID: "
                        + customer.getId()
        );

        return "Customer Created Successfully";
    }


    public List<Customer> getAllCustomers() {

        List<Customer> customers =
                customerRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_CUSTOMERS",
                "CRM",
                "All customers retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All customers retrieved successfully. Count: "
                        + customers.size()
        );

        return customers;
    }


    public Customer getCustomer(Long id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_CUSTOMER",
                "CRM",
                "Customer retrieved successfully. Customer ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer retrieved successfully. Customer ID: "
                        + id
        );

        return customer;
    }


    public String updateCustomer(
            Long id,
            Customer customer) {

        Customer existing = getCustomer(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setCustomerCode(
                customer.getCustomerCode());

        existing.setCustomerName(
                customer.getCustomerName());

        existing.setCompanyName(
                customer.getCompanyName());

        existing.setCustomerType(
                customer.getCustomerType());

        existing.setEmail(
                customer.getEmail());

        existing.setPhone(
                customer.getPhone());

        existing.setAlternatePhone(
                customer.getAlternatePhone());

        existing.setGstNumber(
                customer.getGstNumber());

        existing.setAddress(
                customer.getAddress());

        existing.setCity(
                customer.getCity());

        existing.setState(
                customer.getState());

        existing.setCountry(
                customer.getCountry());

        existing.setPostalCode(
                customer.getPostalCode());

        existing.setStatus(
                customer.getStatus());

        customerRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_CUSTOMER",
                "CRM",
                "Customer updated successfully. Customer ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                performedBy,
                performedBy,
                "Customer updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer updated successfully. Customer ID: "
                        + id
        );

        return "Customer Updated Successfully";
    }


    public String deleteCustomer(Long id) {

        Customer customer = getCustomer(id);

        String oldValue = convertToJson(customer);

        String performedBy = getLoggedInEmployeeId();

        customerRepository.delete(customer);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_CUSTOMER",
                "CRM",
                "Customer deleted successfully. Customer ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                customer.getId().toString(),
                AuditActionType.DELETE,
                performedBy,
                performedBy,
                "Customer deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer deleted successfully. Customer ID: "
                        + id
        );

        return "Customer Deleted Successfully";
    }


// =========================================================
// LEADS
// =========================================================

    public String createLead(
            Long customerId,
            String employeeId,
            Lead lead) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer Not Found"));

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Not Found"));

        lead.setCustomer(customer);
        lead.setAssignedTo(employee);

        leadRepository.save(lead);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(lead);

        auditLogsService.logActivity(
                performedBy,
                "CREATE_LEAD",
                "CRM",
                "Lead created successfully. Lead ID: "
                        + lead.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                lead.getId().toString(),
                AuditActionType.CREATE,
                null,
                performedBy,
                "Lead created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Lead created successfully. Lead ID: "
                        + lead.getId()
        );

        return "Lead Created Successfully";
    }


    public List<Lead> getAllLeads() {

        List<Lead> leads =
                leadRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_LEADS",
                "CRM",
                "All leads retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All leads retrieved successfully. Count: "
                        + leads.size()
        );

        return leads;
    }


    public Lead getLead(Long id) {

        Lead lead =
                leadRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lead Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_LEAD",
                "CRM",
                "Lead retrieved successfully. Lead ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Lead retrieved successfully. Lead ID: "
                        + id
        );

        return lead;
    }


    public String updateLead(
            Long id,
            Lead lead) {

        Lead existing = getLead(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setLeadSource(
                lead.getLeadSource());

        existing.setLeadTitle(
                lead.getLeadTitle());

        existing.setContactPerson(
                lead.getContactPerson());

        existing.setExpectedValue(
                lead.getExpectedValue());

        existing.setPriority(
                lead.getPriority());

        existing.setLeadStatus(
                lead.getLeadStatus());

        existing.setRemarks(
                lead.getRemarks());

        leadRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_LEAD",
                "CRM",
                "Lead updated successfully. Lead ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                performedBy,
                performedBy,
                "Lead updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Lead updated successfully. Lead ID: "
                        + id
        );

        return "Lead Updated Successfully";
    }


    public String deleteLead(Long id) {

        Lead lead = getLead(id);

        String oldValue = convertToJson(lead);

        String performedBy = getLoggedInEmployeeId();

        leadRepository.delete(lead);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LEAD",
                "CRM",
                "Lead deleted successfully. Lead ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                lead.getId().toString(),
                AuditActionType.DELETE,
                performedBy,
                performedBy,
                "Lead deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Lead deleted successfully. Lead ID: "
                        + id
        );

        return "Lead Deleted Successfully";
    }


// =========================================================
// OPPORTUNITIES
// =========================================================

    public String createOpportunity(
            Long leadId,
            String employeeId,
            Opportunity opportunity) {

        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        opportunity.setLead(lead);
        opportunity.setAssignedTo(employee);

        opportunityRepository.save(opportunity);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(opportunity);

        auditLogsService.logActivity(
                performedBy,
                "CREATE_OPPORTUNITY",
                "CRM",
                "Opportunity created successfully. Opportunity ID: "
                        + opportunity.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                opportunity.getId().toString(),
                AuditActionType.CREATE,
                null,
                performedBy,
                "Opportunity created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Opportunity created successfully. Opportunity ID: "
                        + opportunity.getId()
        );

        return "Opportunity Created Successfully";
    }


    public List<Opportunity> getAllOpportunities() {

        List<Opportunity> opportunities =
                opportunityRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_OPPORTUNITIES",
                "CRM",
                "All opportunities retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All opportunities retrieved successfully. Count: "
                        + opportunities.size()
        );

        return opportunities;
    }


    public Opportunity getOpportunity(Long id) {

        Opportunity opportunity =
                opportunityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Opportunity Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_OPPORTUNITY",
                "CRM",
                "Opportunity retrieved successfully. Opportunity ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Opportunity retrieved successfully. Opportunity ID: "
                        + id
        );

        return opportunity;
    }


    public String updateOpportunity(
            Long id,
            Opportunity opportunity) {

        Opportunity existing =
                getOpportunity(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setOpportunityName(
                opportunity.getOpportunityName());

        existing.setEstimatedValue(
                opportunity.getEstimatedValue());

        existing.setProbability(
                opportunity.getProbability());

        existing.setExpectedCloseDate(
                opportunity.getExpectedCloseDate());

        existing.setStage(
                opportunity.getStage());

        existing.setRemarks(
                opportunity.getRemarks());

        opportunityRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_OPPORTUNITY",
                "CRM",
                "Opportunity updated successfully. Opportunity ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                null,
                performedBy,
                "Opportunity updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Opportunity updated successfully. Opportunity ID: "
                        + id
        );

        return "Opportunity Updated Successfully";
    }


    public String deleteOpportunity(Long id) {

        Opportunity opportunity =
                getOpportunity(id);

        String oldValue = convertToJson(opportunity);

        String performedBy = getLoggedInEmployeeId();

        opportunityRepository.delete(opportunity);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_OPPORTUNITY",
                "CRM",
                "Opportunity deleted successfully. Opportunity ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                opportunity.getId().toString(),
                AuditActionType.DELETE,
                performedBy,
                performedBy,
                "Opportunity deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Opportunity deleted successfully. Opportunity ID: "
                        + id
        );

        return "Opportunity Deleted Successfully";
    }


// =========================================================
// QUOTATIONS
// =========================================================

    public String createQuotation(
            Long opportunityId,
            Long customerId,
            String employeeId,
            Quotation quotation) {

        Opportunity opportunity =
                opportunityRepository
                        .findById(opportunityId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Opportunity Not Found"));

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer Not Found"));

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        quotation.setOpportunity(opportunity);
        quotation.setCustomer(customer);
        quotation.setCreatedBy(employee);

        quotationRepository.save(quotation);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(quotation);

        auditLogsService.logActivity(
                performedBy,
                "CREATE_QUOTATION",
                "CRM",
                "Quotation created successfully. Quotation ID: "
                        + quotation.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                quotation.getId().toString(),
                AuditActionType.CREATE,
                performedBy,
                performedBy,
                "Quotation created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Quotation created successfully. Quotation ID: "
                        + quotation.getId()
        );

        return "Quotation Created Successfully";
    }


    public List<Quotation> getAllQuotations() {

        List<Quotation> quotations =
                quotationRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_QUOTATIONS",
                "CRM",
                "All quotations retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All quotations retrieved successfully. Count: "
                        + quotations.size()
        );

        return quotations;
    }


    public Quotation getQuotation(Long id) {

        Quotation quotation =
                quotationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Quotation Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_QUOTATION",
                "CRM",
                "Quotation retrieved successfully. Quotation ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Quotation retrieved successfully. Quotation ID: "
                        + id
        );

        return quotation;
    }


    public String updateQuotation(
            Long id,
            Quotation quotation) {

        Quotation existing =
                getQuotation(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setQuotationNumber(
                quotation.getQuotationNumber());

        existing.setQuotationDate(
                quotation.getQuotationDate());

        existing.setValidUntil(
                quotation.getValidUntil());

        existing.setSubtotal(
                quotation.getSubtotal());

        existing.setTaxAmount(
                quotation.getTaxAmount());

        existing.setDiscount(
                quotation.getDiscount());

        existing.setTotalAmount(
                quotation.getTotalAmount());

        existing.setQuotationStatus(
                quotation.getQuotationStatus());

        quotationRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_QUOTATION",
                "CRM",
                "Quotation updated successfully. Quotation ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                null,
                performedBy,
                "Quotation updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Quotation updated successfully. Quotation ID: "
                        + id
        );

        return "Quotation Updated Successfully";
    }


    public String deleteQuotation(Long id) {

        Quotation quotation =
                getQuotation(id);

        String oldValue = convertToJson(quotation);

        String performedBy = getLoggedInEmployeeId();

        quotationRepository.delete(quotation);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_QUOTATION",
                "CRM",
                "Quotation deleted successfully. Quotation ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                quotation.getId().toString(),
                AuditActionType.DELETE,
                null,
                performedBy,
                "Quotation deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Quotation deleted successfully. Quotation ID: "
                        + id
        );

        return "Quotation Deleted Successfully";
    }


// =========================================================
// FOLLOWUPS
// =========================================================

    public String createFollowup(
            Long customerId,
            Long leadId,
            String employeeId,
            Followup followup) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer Not Found"));

        Lead lead =
                leadRepository
                        .findById(leadId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lead Not Found"));

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        followup.setCustomer(customer);
        followup.setLead(lead);
        followup.setAssignedTo(employee);

        followupRepository.save(followup);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(followup);

        auditLogsService.logActivity(
                performedBy,
                "CREATE_FOLLOWUP",
                "CRM",
                "Followup created successfully. Followup ID: "
                        + followup.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                followup.getId().toString(),
                AuditActionType.CREATE,
                null,
                performedBy,
                "Followup created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Followup created successfully. Followup ID: "
                        + followup.getId()
        );

        return "Followup Created Successfully";
    }


    public List<Followup> getAllFollowups() {

        List<Followup> followups =
                followupRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_FOLLOWUPS",
                "CRM",
                "All followups retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All followups retrieved successfully. Count: "
                        + followups.size()
        );

        return followups;
    }


    public Followup getFollowup(Long id) {

        Followup followup =
                followupRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Followup Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_FOLLOWUP",
                "CRM",
                "Followup retrieved successfully. Followup ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Followup retrieved successfully. Followup ID: "
                        + id
        );

        return followup;
    }


    public String updateFollowup(
            Long id,
            Followup followup) {

        Followup existing =
                getFollowup(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setFollowupDate(
                followup.getFollowupDate());

        existing.setFollowupType(
                followup.getFollowupType());

        existing.setFollowupStatus(
                followup.getFollowupStatus());

        existing.setNotes(
                followup.getNotes());

        existing.setNextFollowupDate(
                followup.getNextFollowupDate());

        followupRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_FOLLOWUP",
                "CRM",
                "Followup updated successfully. Followup ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                null,
                performedBy,
                "Followup updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Followup updated successfully. Followup ID: "
                        + id
        );

        return "Followup Updated Successfully";
    }


    public String deleteFollowup(Long id) {

        Followup followup =
                getFollowup(id);

        String oldValue = convertToJson(followup);

        String performedBy = getLoggedInEmployeeId();

        followupRepository.delete(followup);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_FOLLOWUP",
                "CRM",
                "Followup deleted successfully. Followup ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                followup.getId().toString(),
                AuditActionType.DELETE,
                null,
                performedBy,
                "Followup deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Followup deleted successfully. Followup ID: "
                        + id
        );

        return "Followup Deleted Successfully";
    }


// =========================================================
// CUSTOMER MEETINGS
// =========================================================

    public String createCustomerMeeting(
            Long customerId,
            Long opportunityId,
            String employeeId,
            CustomerMeeting meeting) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer Not Found"));

        Opportunity opportunity =
                opportunityRepository
                        .findById(opportunityId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Opportunity Not Found"));

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        meeting.setCustomer(customer);
        meeting.setOpportunity(opportunity);
        meeting.setCreatedBy(employee);

        customerMeetingRepository.save(meeting);

        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(meeting);

        auditLogsService.logActivity(
                performedBy,
                "CREATE_CUSTOMER_MEETING",
                "CRM",
                "Customer meeting created successfully. Meeting ID: "
                        + meeting.getId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                meeting.getId().toString(),
                AuditActionType.CREATE,
                null,
                performedBy,
                "Customer meeting created successfully",
                null,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer meeting created successfully. Meeting ID: "
                        + meeting.getId()
        );

        return "Customer Meeting Created Successfully";
    }


    public List<CustomerMeeting>
    getAllCustomerMeetings() {

        List<CustomerMeeting> meetings =
                customerMeetingRepository.findAll();

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_CUSTOMER_MEETINGS",
                "CRM",
                "All customer meetings retrieved successfully",
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "All customer meetings retrieved successfully. Count: "
                        + meetings.size()
        );

        return meetings;
    }


    public CustomerMeeting getCustomerMeeting(
            Long id) {

        CustomerMeeting meeting =
                customerMeetingRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer Meeting Not Found"));

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(
                performedBy,
                "GET_CUSTOMER_MEETING",
                "CRM",
                "Customer meeting retrieved successfully. Meeting ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer meeting retrieved successfully. Meeting ID: "
                        + id
        );

        return meeting;
    }


    public String updateCustomerMeeting(
            Long id,
            CustomerMeeting meeting) {

        CustomerMeeting existing =
                getCustomerMeeting(id);

        String oldValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        existing.setMeetingTitle(
                meeting.getMeetingTitle());

        existing.setMeetingDate(
                meeting.getMeetingDate());

        existing.setMeetingTime(
                meeting.getMeetingTime());

        existing.setMeetingLocation(
                meeting.getMeetingLocation());

        existing.setMeetingMode(
                meeting.getMeetingMode());

        existing.setAttendees(
                meeting.getAttendees());

        existing.setMeetingNotes(
                meeting.getMeetingNotes());

        existing.setMeetingStatus(
                meeting.getMeetingStatus());

        customerMeetingRepository.save(existing);

        String newValue = convertToJson(existing);

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_CUSTOMER_MEETING",
                "CRM",
                "Customer meeting updated successfully. Meeting ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                existing.getId().toString(),
                AuditActionType.UPDATE,
                null,
                performedBy,
                "Customer meeting updated successfully",
                oldValue,
                newValue,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer meeting updated successfully. Meeting ID: "
                        + id
        );

        return "Customer Meeting Updated Successfully";
    }


    public String deleteCustomerMeeting(
            Long id) {

        CustomerMeeting meeting =
                getCustomerMeeting(id);

        String oldValue = convertToJson(meeting);

        String performedBy = getLoggedInEmployeeId();

        customerMeetingRepository.delete(meeting);

        auditLogsService.logActivity(
                performedBy,
                "DELETE_CUSTOMER_MEETING",
                "CRM",
                "Customer meeting deleted successfully. Meeting ID: "
                        + id,
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.createAuditLog(
                "CRM",
                meeting.getId().toString(),
                AuditActionType.DELETE,
                null,
                performedBy,
                "Customer meeting deleted successfully",
                oldValue,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        auditLogsService.logInfo(
                "CRM",
                "CRMService",
                "Customer meeting deleted successfully. Meeting ID: "
                        + id
        );

        return "Customer Meeting Deleted Successfully";
    }

}

