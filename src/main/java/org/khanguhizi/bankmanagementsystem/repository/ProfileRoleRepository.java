package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Profile;
import org.khanguhizi.bankmanagementsystem.models.ProfileRole;
import org.khanguhizi.bankmanagementsystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProfileRoleRepository extends JpaRepository<ProfileRole,Integer> {
    Optional<ProfileRole> findByProfileAndRole(Profile profile, Role role);

    List<ProfileRole> findByProfile(Profile profile);
    List<ProfileRole> findByRole(Role role);

    boolean existsByRole(Role role);
    boolean existsByProfile(Profile profile);

    List<ProfileRole>findByProfile_ProfileName(String profileProfileName);

    @Query("SELECT CASE WHEN COUNT(pr) > 0 THEN true ELSE false END " +
            "FROM ProfileRole pr JOIN pr.role r " +
            "WHERE pr.profile = :profileId AND r.role = :roleId")
    boolean existsByProfileIdAndRoleId(@Param("profileId") Integer profileId,
                                    @Param("roleId") Integer roleId);

}
