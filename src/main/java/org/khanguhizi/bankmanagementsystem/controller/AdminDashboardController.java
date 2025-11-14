package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/admin/fetchCustomers")
    public ResponseEntity<ApiResponse> getAllCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse response = adminDashboardService.getAllCustomers(search, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update details of a customer"
    )
    @PutMapping("/admin/updateCustomer/{id}")
    public ResponseEntity<ApiResponse> updateCustomer(
            @PathVariable Integer id,
            @RequestBody Customer updatedData
    ) {
        ApiResponse response = adminDashboardService.updateCustomer(id, updatedData);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Soft deletes a customer"
    )
    @DeleteMapping("/admin/deleteCustomer/{id}")
    public ResponseEntity<ApiResponse> softDeleteCustomer(@PathVariable Integer id) {
        ApiResponse response = adminDashboardService.softDeleteCustomer(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = ("Blocks a customer")
    )
    @PatchMapping("/admin/blockCustomer/{id}")
    public ResponseEntity<ApiResponse> toggleBlockCustomer(
            @PathVariable Integer id,
            @RequestParam boolean block
    ) {
        ApiResponse response = adminDashboardService.toggleBlockCustomer(id, block);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Fetches all transactions in a database.",
            description = "Can be filtered by transaction type and customer Id"
    )
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
    @GetMapping("/transaction{transactionCode}details")
    public ResponseEntity<ApiResponse> getTransactionDetails(
            @PathVariable String transactionCode
    ) {
        ApiResponse response = adminDashboardService.getTransactionDetails(transactionCode);
        return ResponseEntity.ok(response);
    }
}