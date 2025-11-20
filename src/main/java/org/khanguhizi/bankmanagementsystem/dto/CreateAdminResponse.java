package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;

import java.util.List;

@Data
public class CreateAdminResponse {
    private String username;
    private String password;
    private String profileName;
}
