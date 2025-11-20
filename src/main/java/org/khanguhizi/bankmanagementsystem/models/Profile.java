package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Profile")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false )
    private String profileName;
}
