package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Profile_Roles", uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "role_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Data

@Builder
public class ProfileRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
