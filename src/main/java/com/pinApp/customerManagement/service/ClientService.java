package com.pinApp.customerManagement.service;

import com.pinApp.customerManagement.model.Client;
import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {

    private static final int LIFE_EXPECTANCY = 100;

    private final ClientRepository clientRepository;

    ModelMapper modelMapper = new ModelMapper();

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse createClient(ClientRequest request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setAge(request.getAge());
        client.setBirthDate(request.getBirthDate());

        Client savedClient = clientRepository.save(client);

        return modelMapper.map(savedClient, ClientResponse.class);
    }

    public List<ClientResponse> getAllClientsWithLifeExpectancy() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToResponseWithLifeExpectancy)
                .toList();
    }

    private ClientResponse convertToResponseWithLifeExpectancy(Client client) {
        ClientResponse response = modelMapper.map(client, ClientResponse.class);
        response.setLifeExpectancyDate(calculateLifeExpectancy(client.getBirthDate()));
        return response;
    }

    private LocalDate calculateLifeExpectancy(LocalDate birthDate) {
        return birthDate.plusYears(LIFE_EXPECTANCY);
    }
}
