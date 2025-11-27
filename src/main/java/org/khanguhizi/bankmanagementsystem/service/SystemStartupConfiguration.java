package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.ProfileRequest;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.khanguhizi.bankmanagementsystem.models.ProfileRole;
import org.khanguhizi.bankmanagementsystem.models.Role;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRoleRepository;
import org.khanguhizi.bankmanagementsystem.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SystemStartupConfiguration {
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final ProfileRoleRepository profileRoleRepository;

    @Bean
    public CommandLineRunner init(PasswordEncoder passwordEncoder) {
        return args -> {
            if (!profileRepository.existsByProfileName("SUPERADMIN")) {
                var profile = new ProfileRequest();
                profile.setProfileName("SUPERADMIN");
                profileService.createProfile(profile);
            }
            if (!profileRepository.existsByProfileName("CUSTOMER")) {
                var profile = new ProfileRequest();
                profile.setProfileName("CUSTOMER");
                profileService.createProfile(profile);
            }

            List<Role> allRoles = RoleData.getRoles();
            for (Role role : allRoles) {
                if (!roleRepository.existsById(Long.valueOf(role.getId()))) {
                    roleRepository.save(role);
                }
            }

            // create super admin if not exists
            if (!customerRepository.existsByUsername("superadmin")) {
                var profile = new Customer();
                profile.setUsername("superadmin");
                profile.setFirstName("Superadmin");
                profile.setEmail("admin@bank.com");
                profile.setPassword(passwordEncoder.encode("Admin@123"));
                profile.setPhoneNumber("070000000000");
                profile.setLastName("Admin");
                profile.setDateOfBirth(LocalDate.now());
                profile.setNationalId("System");
                profile.setDeleted(false);
                profile.setBlocked(false);
                profile.setProfile(profileRepository.findByProfileName("SUPERADMIN").get());
                customerRepository.save(profile);
            }
           assignRolesToSuperadmin();
        };
    }
    public void assignRolesToSuperadmin() {
        Profile superadmin = profileRepository.findByProfileName("SUPERADMIN")
                .orElseThrow(() -> new RuntimeException("Superadmin profile not found"));

        List<Role> allRoles = roleRepository.findAll();

        for (Role role : allRoles) {
            boolean alreadyAssigned = profileRoleRepository.findByProfileAndRole(superadmin, role).isPresent();

            if (!alreadyAssigned) {
                ProfileRole profileRole = new ProfileRole();
                profileRole.setProfile(superadmin);
                profileRole.setRole(role);
                profileRoleRepository.save(profileRole);
            }
        }
    }

    public class RoleData {
        public static List<Role> getRoles() {
            return Arrays.asList(
                    new Role(1, "CREATE_ACCOUNT"),
                    new Role(2, "READ_ACCOUNT"),
                    new Role(3, "DELETE_ACCOUNT"),
                    new Role(4, "UPDATE_ACCOUNT"),
                    new Role(5, "CREATE_CUSTOMER"),
                    new Role(6, "READ_CUSTOMER"),
                    new Role(7, "DELETE_CUSTOMER"),
                    new Role(8, "UPDATE_CUSTOMER"),
                    new Role(9, "CREATE_ADMIN"),
                    new Role(10, "READ_ADMIN"),
                    new Role(11, "DELETE_ADMIN"),
                    new Role(12, "UPDATE_ADMIN"),
                    new Role(13, "CREATE_ACCOUNT_TYPES"),
                    new Role(14, "READ_ACCOUNT_TYPES"),
                    new Role(15, "DELETE_ACCOUNT_TYPES"),
                    new Role(16, "UPDATE_ACCOUNT_TYPES"),
                    new Role(17, "UPDATE_TRANSACTION_COSTS"),
                    new Role(18, "READ_TRANSACTION_COSTS"),
                    new Role(19, "CREATE_ROLES"),
                    new Role(20, "UPDATE_ROLES"),
                    new Role(21, "READ_ROLES"),
                    new Role(22, "DELETE_ROLES"),
                    new Role(23, "CREATE_PROFILES"),
                    new Role(24, "UPDATE_PROFILES"),
                    new Role(25, "DELETE_PROFILES"),
                    new Role(26, "READ_PROFILES"),
                    new Role(27, "READ_TRANSACTION_COSTS"),
                    new Role(28, "READ_ACCOUNT_TYPES"),
                    new Role(29, "CREATE_TRANSACTION"),
                    new Role(30, "READ_TRANSACTION")
            );
        }
    }


}
