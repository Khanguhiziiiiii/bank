package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;

@Data
public class ProfileRequest {
    private Long profileId;
    private String profileName;
    private Integer customerId;
}
