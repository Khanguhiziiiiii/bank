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
    private Customer customer;
    private Accounts account;
    private Transactions transaction;

    private Summary summary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private AccountsSummary accounts;
        private TransactionsSummary transactions;

        private double bankBalance;
        private double moneyIn;
        private double moneyOut;

        private long totalCustomers;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountsSummary {
        private long total;
        private long checking;
        private long savings;
        private long junior;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionsSummary {
        private long total;
        private long deposit;
        private long withdraw;
        private long transfer;
        private long checkBalance;
    }
}

