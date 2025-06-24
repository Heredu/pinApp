package com.pinApp.customerManagement.service;

import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.model.dto.MetricsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IClientService {
    ClientResponse createClient(ClientRequest request);
    List<ClientResponse> getAllClientsWithLifeExpectancy();
    MetricsResponse getClientMetrics();
}
