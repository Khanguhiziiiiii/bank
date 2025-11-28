package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.service.AdminDashboardService;
import org.khanguhizi.bankmanagementsystem.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.khanguhizi.bankmanagementsystem.dto.*;

@RestController
@Tag(name = "Admin Dashboard", description = "Endpoints for admin dashboard analytics")
@SecurityRequirement(name = "Bearer Token")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @Operation(
            summary = "Get admin dashboard data",
            description = "Fetches key statistics and aggregated data for the admin dashboard."
    )
    @GetMapping("/admin/dashboard")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> getDashboardData() {
        var dashboardData = adminDashboardService.getDashboardData();
        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }

    @Operation(
            summary = "Fetches details of a customer"
    )
@PreAuthorize("hasRole('READ_CUSTOMER')")
    @GetMapping("/admin/fetchAllUsers")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse response = adminDashboardService.getAllUsers(search, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update details of a user"
    )
@PreAuthorize("hasRole('UPDATE_CUSTOMER')")
    @PutMapping("/admin/updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody Customer updatedData
    ) {
        ApiResponse response = adminDashboardService.updateUser(id, updatedData);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Soft deletes a user"
    )
@PreAuthorize("hasRole('DELETE_CUSTOMER')")
    @DeleteMapping("/admin/deleteUser/{id}")
    public ResponseEntity<ApiResponse> softDeleteUser(@PathVariable Integer id) {
        ApiResponse response = adminDashboardService.softDeleteUser(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = ("Blocks a customer")
    )
@PreAuthorize("hasRole('UPDATE_CUSTOMER')")
    @PatchMapping("/admin/blockUser/{id}")
    public ResponseEntity<ApiResponse> toggleBlockUser(
            @PathVariable Integer id,
            @RequestParam boolean block
    ) {
        ApiResponse response = adminDashboardService.toggleBlockUser(id, block);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Fetches all transactions in a database.",
            description = "Can be filtered by transaction type and customer Id"
    )
@PreAuthorize("hasRole('READ_TRANSACTION')")
    @GetMapping ("/fetchTransactions")
    public ResponseEntity<ApiResponse> getAllTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer customerId
    ) {
        ApiResponse response = adminDashboardService.getAllTransactions(type, customerId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "fetches the details about a transaction"
    )
@PreAuthorize("hasRole('READ_TRANSACTION')")
    @GetMapping("/transaction{transactionCode}details")
    public ResponseEntity<ApiResponse> getTransactionDetails(
            @PathVariable String transactionCode
    ) {
        ApiResponse response = adminDashboardService.getTransactionDetails(transactionCode);
        return ResponseEntity.ok(response);
    }

}