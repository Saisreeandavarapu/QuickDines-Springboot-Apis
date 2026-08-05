package com.HRMS.QuickDines.CRM.Service;

import com.HRMS.QuickDines.CRM.model.*;
import com.HRMS.QuickDines.CRM.repo.*;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
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

        return "Customer Created Successfully";
    }


    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }


    public Customer getCustomer(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer Not Found"));
    }


    public String updateCustomer(
            Long id,
            Customer customer) {

        Customer existing = getCustomer(id);

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

        return "Customer Updated Successfully";
    }


    public String deleteCustomer(Long id) {

        Customer customer = getCustomer(id);

        customerRepository.delete(customer);

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

        return "Lead Created Successfully";
    }


    public List<Lead> getAllLeads() {

        return leadRepository.findAll();
    }


    public Lead getLead(Long id) {

        return leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead Not Found"));
    }


    public String updateLead(
            Long id,
            Lead lead) {

        Lead existing = getLead(id);

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

        return "Lead Updated Successfully";
    }


    public String deleteLead(Long id) {

        Lead lead = getLead(id);

        leadRepository.delete(lead);

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

        return "Opportunity Created Successfully";
    }


    public List<Opportunity> getAllOpportunities() {

        return opportunityRepository.findAll();
    }


    public Opportunity getOpportunity(Long id) {

        return opportunityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Opportunity Not Found"));
    }


    public String updateOpportunity(
            Long id,
            Opportunity opportunity) {

        Opportunity existing =
                getOpportunity(id);

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

        return "Opportunity Updated Successfully";
    }


    public String deleteOpportunity(Long id) {

        Opportunity opportunity =
                getOpportunity(id);

        opportunityRepository.delete(opportunity);

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

        return "Quotation Created Successfully";
    }


    public List<Quotation> getAllQuotations() {

        return quotationRepository.findAll();
    }


    public Quotation getQuotation(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quotation Not Found"));
    }


    public String updateQuotation(
            Long id,
            Quotation quotation) {

        Quotation existing =
                getQuotation(id);

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

        return "Quotation Updated Successfully";
    }


    public String deleteQuotation(Long id) {

        Quotation quotation =
                getQuotation(id);

        quotationRepository.delete(quotation);

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

        return "Followup Created Successfully";
    }


    public List<Followup> getAllFollowups() {

        return followupRepository.findAll();
    }


    public Followup getFollowup(Long id) {

        return followupRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Followup Not Found"));
    }


    public String updateFollowup(
            Long id,
            Followup followup) {

        Followup existing =
                getFollowup(id);

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

        return "Followup Updated Successfully";
    }


    public String deleteFollowup(Long id) {

        Followup followup =
                getFollowup(id);

        followupRepository.delete(followup);

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

        return "Customer Meeting Created Successfully";
    }


    public List<CustomerMeeting>
    getAllCustomerMeetings() {

        return customerMeetingRepository.findAll();
    }


    public CustomerMeeting getCustomerMeeting(
            Long id) {

        return customerMeetingRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer Meeting Not Found"));
    }


    public String updateCustomerMeeting(
            Long id,
            CustomerMeeting meeting) {

        CustomerMeeting existing =
                getCustomerMeeting(id);

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

        return "Customer Meeting Updated Successfully";
    }


    public String deleteCustomerMeeting(
            Long id) {

        CustomerMeeting meeting =
                getCustomerMeeting(id);

        customerMeetingRepository.delete(meeting);

        return "Customer Meeting Deleted Successfully";
    }
}

