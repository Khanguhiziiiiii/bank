package org.khanguhizi.bankmanagementsystem.utilities;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.exceptions.UnauthorizedAccessException;
import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.repository.AccountRepository;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtility {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public Customer getAuthenticatedCustomer() {  //Retrieves the customer associated with the currently logged-in user
        var auth = SecurityContextHolder.getContext().getAuthentication(); //Fetches the current authentication information from Spring Security's context
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }  //Checks if user has been authenticated

        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = auth.getName();
        }
        /*
            auth.getPrincipal() returns the user details object — which could be a UserDetails instance or just a String username
            (depending on your security setup).
            This code safely handles both cases:
                If it’s a UserDetails object, extract the username using getUsername().
                Otherwise, use auth.getName().
         */

        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedAccessException("Customer not found for the logged-in user"));
    }
        /*
            Looks up the Customer in the database using the email (username from the token).
            If the customer doesn’t exist, it throws an UnauthorizedAccessException.
            Result: You now have a Customer object that represents the authenticated user.
         */


    public void verifyAccountOwnership(String accountNumber) {  //This method checks whether the authenticated customer actually owns the account they’re trying to use (e.g., for a deposit or withdrawal).
        Customer currentCustomer = getAuthenticatedCustomer(); //Calls the previous method to get the logged-in Customer entity.

        Accounts account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new UnauthorizedAccessException("Account not found"));
        /*
            Finds the account in the database using the provided account number.
            If no account is found, throw UnauthorizedAccessException.
         */

        if (account.getCustomer().getId().equals(currentCustomer.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to perform transactions on this account");
        }

        /*
            Compares the customer ID of the account with the ID of the authenticated customer.
            If they don’t match, it means the logged-in user is trying to access another person’s account.
            In that case, throw an exception.
         */
    }
}



