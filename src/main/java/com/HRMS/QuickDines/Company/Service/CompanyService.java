package com.HRMS.QuickDines.Company.Service;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.BranchLocation;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchLocationRepository;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final BranchLocationRepository branchLocationRepository;


    //=========================================================
    // COMPANY MANAGEMENT
    //=========================================================

    public String createCompany(Company company) {

        companyRepository.save(company);

        return "Company Created Successfully";
    }


    public List<Company> getAllCompanies() {

        return companyRepository.findAll();
    }


    public Company getCompany(Long id) {

        return companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));
    }


    public String updateCompany(Long id, Company company) {

        Company existingCompany = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Not Found"));

        existingCompany.setCompanyCode(company.getCompanyCode());

        existingCompany.setCompanyName(company.getCompanyName());

        existingCompany.setLegalName(company.getLegalName());

        existingCompany.setGstNumber(company.getGstNumber());

        existingCompany.setPanNumber(company.getPanNumber());

        existingCompany.setCinNumber(company.getCinNumber());

        existingCompany.setEmail(company.getEmail());

        existingCompany.setPhone(company.getPhone());

        existingCompany.setWebsite(company.getWebsite());

        existingCompany.setAddress(company.getAddress());

        existingCompany.setCity(company.getCity());

        existingCompany.setState(company.getState());

        existingCompany.setCountry(company.getCountry());

        existingCompany.setPostalCode(company.getPostalCode());

        existingCompany.setLogoUrl(company.getLogoUrl());

        existingCompany.setStatus(company.getStatus());

        companyRepository.save(existingCompany);

        return "Company Updated Successfully";
    }


    public String deleteCompany(Long id) {

        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Not Found"));

        companyRepository.delete(company);

        return "Company Deleted Successfully";
    }


    //=========================================================
    // BRANCH MANAGEMENT
    //=========================================================

    public String createBranch(Long companyId, Branch branch) {

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company Not Found"));
        branch.setCompany(company);

        branchRepository.save(branch);

        return "Branch Created Successfully";
    }


    public List<Branch> getAllBranches() {

        return branchRepository.findAll();
    }


    public Branch getBranch(Long id) {

        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Not Found"));
    }


    public List<Branch> getBranchesByCompany(Long companyId) {

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company Not Found"));

        return branchRepository.findByCompany(company);
    }


    public String updateBranch(Long id, Branch branch) {

        Branch existingBranch = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Not Found"));

        existingBranch.setBranchCode(branch.getBranchCode());

        existingBranch.setBranchName(branch.getBranchName());

        existingBranch.setBranchType(branch.getBranchType());

        existingBranch.setManager(branch.getManager());

        existingBranch.setEmail(branch.getEmail());
        existingBranch.setPhone(branch.getPhone());

        existingBranch.setAddress(branch.getAddress());

        existingBranch.setCity(branch.getCity());

        existingBranch.setState(branch.getState());

        existingBranch.setCountry(branch.getCountry());

        existingBranch.setPostalCode(branch.getPostalCode());

        existingBranch.setStatus(branch.getStatus());

        branchRepository.save(existingBranch);

        return "Branch Updated Successfully";
    }


    public String deleteBranch(Long id) {

        Branch branch = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Not Found"));

        branchRepository.delete(branch);

        return "Branch Deleted Successfully";
    }


    //=========================================================
    // BRANCH LOCATION MANAGEMENT
    //=========================================================

    public String createBranchLocation(Long branchId, BranchLocation location) {

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch Not Found"));

        location.setBranch(branch);

        branchLocationRepository.save(location);

        return "Branch Location Created Successfully";
    }


    public List<BranchLocation> getAllBranchLocations() {

        return branchLocationRepository.findAll();
    }


    public BranchLocation getBranchLocation(Long id) {

        return branchLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Location Not Found"));
    }


    public List<BranchLocation> getLocationsByBranch(Long branchId) {

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch Not Found"));

        return branchLocationRepository.findByBranch(branch);
    }


    public String updateBranchLocation(Long id, BranchLocation location) {

        BranchLocation existingLocation = branchLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Location Not Found"));

        existingLocation.setBuildingName(location.getBuildingName());

        existingLocation.setFloor(location.getFloor());

        existingLocation.setAddressLine1(location.getAddressLine1());

        existingLocation.setAddressLine2(location.getAddressLine2());

        existingLocation.setCity(location.getCity());

        existingLocation.setState(location.getState());

        existingLocation.setCountry(location.getCountry());

        existingLocation.setPostalCode(location.getPostalCode());

        existingLocation.setLatitude(location.getLatitude());

        existingLocation.setLongitude(location.getLongitude());

        existingLocation.setGeoRadius(location.getGeoRadius());

        branchLocationRepository.save(existingLocation);

        return "Branch Location Updated Successfully";
    }


    public String deleteBranchLocation(Long id) {

        BranchLocation location = branchLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch Location Not Found"));

        branchLocationRepository.delete(location);

        return "Branch Location Deleted Successfully";
    }
}