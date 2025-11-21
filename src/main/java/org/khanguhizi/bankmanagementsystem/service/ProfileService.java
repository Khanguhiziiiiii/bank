package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

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
}
