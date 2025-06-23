package com.pinApp.customerManagement.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
}
