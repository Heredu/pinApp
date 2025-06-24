package com.pinApp.customerManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;

}
