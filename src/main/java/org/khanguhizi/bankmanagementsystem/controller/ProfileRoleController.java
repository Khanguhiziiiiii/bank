package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.service.ProfileRoleService;
import org.khanguhizi.bankmanagementsystem.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "Profile-Role Controller", description = "Endpoints for profiles and roles")
@SecurityRequirement(name = "Bearer Token")
public class ProfileRoleController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRoleService profileRoleService;

    @Operation(
            summary = "creates profiles"
    )
    @PostMapping("/superadmin/createProfile")
    public ResponseEntity<ApiResponse> createProfile(@RequestBody ProfileRequest profileRequest) {
        var createProfileRes = profileService.createProfile(profileRequest);
        return new ResponseEntity<>(createProfileRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "creates admins"
    )
    @PostMapping("/superadmin/createAdmin")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody CreateAdminRequest request) {
        var createAdminRes = profileRoleService.createAdmin(request);
        return new ResponseEntity<>(createAdminRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Gets Profile roles"
    )
    @GetMapping("/superadmin/profileRoles")
    public ResponseEntity<List<ProfileRoleResponse>> getAllProfileRoles() {
        var list = profileRoleService.getAllProfileRoles();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/superadmin/removeRoleFromProfile")
    public ResponseEntity<ApiResponse> removeRoleFromProfile(
            @RequestBody ProfileRoleRequest request) {

        ApiResponse response = profileRoleService.removeRoleFromProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/superadmin/fetchProfileRoles")
    public ResponseEntity<ApiResponse> fetchProfileRoles(
            @RequestBody ProfileRoleRequest request) {

        ApiResponse response = profileRoleService.fetchProfileRoles(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/superadmin/fetchRoles")
    public ResponseEntity<ApiResponse> fetchRoles() {
        ApiResponse response = profileRoleService.fetchRoles();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/superadmin/fetchProfiles")
    public ResponseEntity<ApiResponse> fetchProfiles() {

        ApiResponse response = profileRoleService.fetchProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

