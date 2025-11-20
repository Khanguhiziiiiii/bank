package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.khanguhizi.bankmanagementsystem.models.ProfileRole;
import org.khanguhizi.bankmanagementsystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRoleRepository extends JpaRepository<ProfileRole,Integer> {
    Optional<ProfileRole> findByProfileAndRole(Profile profile, Role role);

    List<ProfileRole> findByProfile(Profile profile);
}
