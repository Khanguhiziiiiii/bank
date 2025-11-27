package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.khanguhizi.bankmanagementsystem.models.ProfileRole;
import org.khanguhizi.bankmanagementsystem.models.Role;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRoleRepository;
import org.khanguhizi.bankmanagementsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileRoleService {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final CustomerRepository customerRepository;
    @Autowired
    @Lazy
    private  PasswordEncoder passwordEncoder;

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

    public ApiResponse assignRoleToProfile(ProfileRoleRequest request) {
        Optional<Profile> existingProfile = profileRepository.findById(request.getProfileId());
        if (existingProfile.isEmpty()) {
            throw new RuntimeException("Profile Not Found!");
        }

        Optional<Role> existingRole = roleRepository.findById(request.getRoleId());
        if (existingRole.isEmpty()) {
            throw new RuntimeException("Role Not Found!");
        }

        Profile profile = existingProfile.get();
        Role role = existingRole.get();

        ProfileRole profileRole = ProfileRole.builder()
                .profile(profile)
                .role(role)
                .build();

        profileRoleRepository.save(profileRole);

        ProfileRoleResponse profileRoleResponse = new ProfileRoleResponse();
        profileRoleResponse.setProfileName(profile.getProfileName());
        profileRoleResponse.setRole(role.getRole());

        return ApiResponse.builder()
                .message("Role Assigned Successfully!")
                .data(profileRoleResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse removeRoleFromProfile(ProfileRoleRequest request) {
        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        ProfileRole profileRole = profileRoleRepository
                .findByProfileAndRole(profile, role)
                .orElse(null);

        if (profileRole == null) {
            return ApiResponse.builder()
                    .message("Mapping does not exist")
                    .status(String.valueOf(HttpStatus.NOT_FOUND))
                    .build();
        }

        profileRoleRepository.delete(profileRole);

        return ApiResponse.builder()
                .message("Role removed from profile successfully")
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchRoles() {
        List<Role> allRoles = roleRepository.findAll();

        return ApiResponse.builder()
                .message("Fetched all roles successfully")
                .data(allRoles)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchProfiles() {
        List<Profile> allProfiles = profileRepository.findAll();

        return ApiResponse.builder()
                .message("Fetched all profiles successfully")
                .data(allProfiles)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchRolesAssignedToAProfile(ProfileRoleRequest request) {

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<ProfileRole> profileRoles = profileRoleRepository.findByProfile(profile);

        return ApiResponse.builder()
                .message("Fetched profile roles successfully")
                .data(profileRoles)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean hasAssignments = profileRoleRepository.existsByRole(role);
        if (hasAssignments) {
            throw new RuntimeException("Role cannot be deleted because it is assigned to profiles");
        }

        roleRepository.delete(role);

        return ApiResponse.builder()
                .message("Role deleted successfully")
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse deleteProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        boolean hasRoleMappings = profileRoleRepository.existsByProfile(profile);
        if (hasRoleMappings) {
            throw new RuntimeException("Profile cannot be deleted because roles are assigned to it");
        }

        profileRepository.delete(profile);

        return ApiResponse.builder()
                .message("Profile deleted successfully")
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchProfilesAssignedToRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<ProfileRole> mappings = profileRoleRepository.findByRole(role);

        List<Profile> profiles = mappings.stream()
                .map(ProfileRole::getProfile)
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .message("Fetched profiles assigned to the role successfully")
                .data(profiles)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}

