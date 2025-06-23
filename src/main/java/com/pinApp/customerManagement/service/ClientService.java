package com.pinApp.customerManagement.service;

import com.pinApp.customerManagement.model.Client;
import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

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
}
