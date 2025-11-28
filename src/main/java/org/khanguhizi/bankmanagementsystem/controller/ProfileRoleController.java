package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.service.ProfileRoleService;
import org.khanguhizi.bankmanagementsystem.service.ProfileService;
import org.khanguhizi.bankmanagementsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private RoleService roleService;

    @Operation(
            summary = "creates profiles"
    )
@PreAuthorize("hasRole('CREATE_PROFILES')")
    @PostMapping("/createProfile")
    public ResponseEntity<ApiResponse> createProfile(@RequestBody ProfileRequest profileRequest) {
        var createProfileRes = profileService.createProfile(profileRequest);
        return new ResponseEntity<>(createProfileRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "creates admins"
    )
@PreAuthorize("hasRole('CREATE_ADMIN')")
    @PostMapping("/createAdmin")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody CreateAdminRequest request) {
        var createAdminRes = profileRoleService.createAdmin(request);
        return new ResponseEntity<>(createAdminRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Gets Profile roles"
    )
@PreAuthorize("hasRole('UPDATE_PROFILES')")
    @GetMapping("/profileRoles")
    public ResponseEntity<List<ProfileRoleResponse>> getAllProfileRoles() {
        var list = profileRoleService.getAllProfileRoles();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(
            summary = "allows you to remove a role assigned to a profile"
    )
@PreAuthorize("hasRole('UPDATE_PROFILES')")
    @PostMapping("/removeRoleFromProfile")
    public ResponseEntity<ApiResponse> removeRoleFromProfile(
            @RequestBody ProfileRoleRequest request) {

        ApiResponse response = profileRoleService.removeRoleFromProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "allows you to read assigned to profiles"
    )
@PreAuthorize("hasRole('READ_PROFILES')")
    @PostMapping("/fetchRolesAssignedToProfile")
    public ResponseEntity<ApiResponse> fetchRolesAssignedToAProfile(
            @RequestBody ProfileRoleRequest request) {

        ApiResponse response = profileRoleService.fetchRolesAssignedToAProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(
            summary = "Allows you to view all the roles in the system"
    )
@PreAuthorize("hasRole('READ_ROLES')")
    @GetMapping("/fetchRoles")
    public ResponseEntity<ApiResponse> fetchRoles() {
        ApiResponse response = profileRoleService.fetchRoles();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Allows you to view the profiles in the system"
    )
@PreAuthorize("hasRole('READ_PROFILES')")
    @GetMapping("/fetchProfiles")
    public ResponseEntity<ApiResponse> fetchProfiles() {

        ApiResponse response = profileRoleService.fetchProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Allows an admin to assign roles to profiles"
    )
@PreAuthorize("hasRole('UPDATE_PROFILES')")
    @PostMapping("/assignRolesToProfiles")
    public ResponseEntity<ApiResponse> assignRolesToProfile(
            @RequestBody ProfileRoleRequest request) {
        var assignRolesRes = profileRoleService.assignRoleToProfile(request);
        return new ResponseEntity<>(assignRolesRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Allows an admin to create roles"
    )
@PreAuthorize("hasRole('CREATE_ROLES')")
    @PostMapping("/createRole")
    public ResponseEntity<ApiResponse> createRoles(
            @RequestBody RoleRequest request) {
        var createRolesRes = roleService.createRole(request);
        return new ResponseEntity<>(createRolesRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Fetch profiles assigned to a given role"
    )
@PreAuthorize("hasRole('READ_ROLES')")
    @GetMapping("/role{roleId}/profiles")
    public ResponseEntity<ApiResponse> getProfilesAssignedToRole(@PathVariable Long roleId) {
        ApiResponse response = profileRoleService.fetchProfilesAssignedToRole(roleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes a role if not assigned to any profile"
    )
    @PreAuthorize("hasRole('DELETE_ROLES')")
    @DeleteMapping("/deleteRole/{roleId}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long roleId) {
        ApiResponse response = profileRoleService.deleteRole(roleId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deletes a profile if not used by any customer or role mapping"
    )
@PreAuthorize("hasRole('DELETE_PROFILES')")
    @DeleteMapping("/deleteProfile/{profileId}")
    public ResponseEntity<ApiResponse> deleteProfile(@PathVariable Long profileId) {
        ApiResponse response = profileRoleService.deleteProfile(profileId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Assign profile to user"
    )
 @PreAuthorize("hasRole('UPDATE_CUSTOMER')")
 @PostMapping("/assignProfileToUser")
    public ResponseEntity<ApiResponse> assignProfileToUser(@RequestBody ProfileRequest request){
        var assignRes = profileService.assignProfileToUser(request);
        return new ResponseEntity<>(assignRes, HttpStatus.OK);
    }

}

