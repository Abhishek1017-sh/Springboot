package com.vms.dto;

import lombok.Data;
import java.util.List;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private List<String> skills; // For volunteers
}
