package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByProfileName(String profileName);
    List<Profile> findAll();

    boolean existsByProfileName(String profileName);

    Optional<Profile> findById(Long id);
}
