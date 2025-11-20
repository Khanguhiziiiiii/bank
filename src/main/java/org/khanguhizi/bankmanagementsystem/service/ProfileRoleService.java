package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.dto.CreateAdminRequest;
import org.khanguhizi.bankmanagementsystem.dto.CreateAdminResponse;
import org.khanguhizi.bankmanagementsystem.dto.ProfileRoleResponse;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.khanguhizi.bankmanagementsystem.models.ProfileRole;
import org.khanguhizi.bankmanagementsystem.models.Role;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRoleRepository;
import org.khanguhizi.bankmanagementsystem.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileRoleService {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse createAdmin(CreateAdminRequest request) {

        Profile profile = profileRepository.findByProfileName(request.getProfileName())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setFirstName("ADMIN");
        customer.setLastName("ADMIN");
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setNationalId(request.getNationalId());
        customer.setDateOfBirth(LocalDate.now());
        customer.setProfile(profile);
        customer.setDeleted(false);
        customer.setBlocked(false);
        customerRepository.save(customer);


        CreateAdminResponse createAdminResponse = new CreateAdminResponse();
        createAdminResponse.setUsername(request.getUsername());
        createAdminResponse.setPassword(request.getPassword());
        createAdminResponse.setProfileName(request.getProfileName());

        return ApiResponse.builder()
                .message("Admin Creation Successful!")
                .data(createAdminResponse)
                .status(String.valueOf(HttpStatus.CREATED))
                .build();
    }

    public List<ProfileRoleResponse> getAllProfileRoles() {
        List<ProfileRole> list = profileRoleRepository.findAll();

        return list.stream()
                .map(pr -> new ProfileRoleResponse(
                        pr.getProfile().getProfileName(),
                        pr.getRole().getRole()
                ))
                .collect(Collectors.toList());
    }
}

