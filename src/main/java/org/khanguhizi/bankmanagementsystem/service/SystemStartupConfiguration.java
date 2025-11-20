package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.ProfileRequest;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class SystemStartupConfiguration {
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final CustomerRepository customerRepository;

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
        };
    }
}
