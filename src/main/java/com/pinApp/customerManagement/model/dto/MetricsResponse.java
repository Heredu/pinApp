package com.pinApp.customerManagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricsResponse {
    private double averageAge;
    private double standardDeviation;
    private long totalClients;
}
