package com.HRMS.QuickDines.Auth.services;


import com.HRMS.QuickDines.Auth.model.Users;
import com.HRMS.QuickDines.Auth.model.Users;
import com.HRMS.QuickDines.Auth.repo.UserRepository;
import com.HRMS.QuickDines.Auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
        Users user= userRepository.findByEmployeeId(employeeId).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return (UserDetails) user;

    }


}