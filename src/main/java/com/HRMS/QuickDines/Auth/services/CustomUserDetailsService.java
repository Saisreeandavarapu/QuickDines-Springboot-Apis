package com.HRMS.QuickDines.Auth.services;


import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {


    private final EmployeeRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {

        Employee user= userRepository.findByEmployeeId(employeeId).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return (UserDetails) user;

    }


}