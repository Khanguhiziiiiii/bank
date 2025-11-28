package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.khanguhizi.bankmanagementsystem.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String Role);

    Optional<Role> findById(Long id);

    boolean existsByRole(String role);

}
