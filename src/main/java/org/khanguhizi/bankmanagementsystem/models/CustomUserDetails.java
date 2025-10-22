package org.khanguhizi.bankmanagementsystem.models;

import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.springframework.security.core.GrantedAuthority;  //used to specify Roles
import org.springframework.security.core.authority.SimpleGrantedAuthority; //implementation of GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Customer customer;

    public CustomUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
        //Returns the user’s (hashed) password from the database.
        //Spring Security uses this to compare against the login password.
    }

    @Override
    public String getUsername() {
        return customer.getUsername(); // or email if preferred
        //Returns the username used to identify the user.
        //This is the unique identifier that the login form uses.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //Returns true if the account is still valid.
        //If you want accounts to expire after a certain time, you’d change this.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //Returns true if the account is not locked.
        //You can lock accounts after too many failed logins, for example.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //Returns true if the credentials (password) are still valid.
        //You could expire credentials after 90 days, for example.
    }

    @Override
    public boolean isEnabled() {
        return true;
        //Returns true if the user is enabled (active).
        //You might disable accounts after registration or for suspicious activity.
    }

    public Customer getCustomer() {
        return customer;
    }
        //Provides access to the full Customer object.
        //Useful if you need to retrieve more than just username/password (e.g., ID, email) later in the app.

}
