package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final CustomerRepository customerRepository;

    public ApiResponse createProfile(ProfileRequest request) {
        Optional<Profile> existingProfile = profileRepository.findByProfileName(request.getProfileName());
        if (existingProfile.isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        Profile profile = Profile.builder()
                .profileName(request.getProfileName())
                .build();

        profileRepository.save(profile);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setProfileName(request.getProfileName().toUpperCase());

        return ApiResponse.builder()
                .message("Profile Creation Successful!")
                .data(profileResponse)
                .status(String.valueOf(HttpStatus.CREATED))
                .build();
    }

    public ApiResponse assignProfileToUser(ProfileRequest request) {
        Optional<Profile> existingProfile = profileRepository.findById(request.getProfileId());
        if (existingProfile.isEmpty()) {
            throw new RuntimeException("Profile Not Found!");
        }

        Profile profile = existingProfile.get();

        Optional<Customer> existingCustomer = customerRepository .findById(request.getCustomerId());
        if (existingCustomer.isEmpty()) {
            throw new RuntimeException("Customer Not Found!");
        }

        Customer customer = existingCustomer.get();

        customer.setProfile(profile);
        customerRepository.save(customer);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setProfileName(profile.getProfileName().toUpperCase());
        profileResponse.setCustomer(customer.getFirstName());

        return ApiResponse.builder()
                .message("Profile Assigned Successfully!")
                .data(profileResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
