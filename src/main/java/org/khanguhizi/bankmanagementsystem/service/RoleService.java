package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public ApiResponse createRole(RoleRequest request) {
        Optional<Role> existingRole = roleRepository.findByRole(request.getRole());
        if (existingRole.isPresent()) {
            throw new RuntimeException("Role already exists");
        }

        Role role = Role.builder()
                .role(request.getRole())
                .build();

        roleRepository.save(role);

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRole(role.getRole().toUpperCase());

        return ApiResponse.builder()
                .message("Role Creation Successful!")
                .data(roleResponse)
                .status(String.valueOf(HttpStatus.CREATED))
                .build();
    }
}
