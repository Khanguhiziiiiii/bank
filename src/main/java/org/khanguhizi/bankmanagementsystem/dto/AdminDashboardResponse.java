package org.khanguhizi.bankmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.Transactions;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardResponse {
    private List<Customer> customers;
    private Map<String, List<Accounts>> accounts;
    private List<Transactions> transactions;
    private Summary summary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {
        private long totalCustomers;
        private long totalTransactions;
        private long totalAccounts;
        private double bankBalance;
        private double moneyIn;
        private double moneyOut;
    }
}
