package com.pinApp.customerManagement.model.dto;

import lombok.Data;

@Data
public class MetricsResponse {
    private double averageAge;
    private double standardDeviation;
    private long totalClients;
}
