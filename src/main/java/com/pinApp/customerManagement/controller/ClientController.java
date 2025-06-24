package com.pinApp.customerManagement.controller;

import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.model.dto.MetricsResponse;
import com.pinApp.customerManagement.service.IClientService;
import com.pinApp.customerManagement.service.impl.ClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final IClientService clientService;

    public ClientController(ClientServiceImpl clientServiceImpl) {
        this.clientService = clientServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        ClientResponse response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> clients = clientService.getAllClientsWithLifeExpectancy();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> getClientMetrics() {
        MetricsResponse metrics = clientService.getClientMetrics();
        return ResponseEntity.ok(metrics);
    }
}
