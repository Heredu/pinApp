package com.pinApp.customerManagement.service.impl;

import com.pinApp.customerManagement.model.Client;
import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.model.dto.MetricsResponse;
import com.pinApp.customerManagement.mq.model.EmailEvent;
import com.pinApp.customerManagement.mq.publisher.Publisher;
import com.pinApp.customerManagement.repository.ClientRepository;
import com.pinApp.customerManagement.service.IClientService;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {

    @Value("${alert.email}")
    private String email;

    private static final int LIFE_EXPECTANCY = 100;

    private final ClientRepository clientRepository;

    private final Publisher publisher;

    ModelMapper modelMapper = new ModelMapper();

    public ClientServiceImpl(ClientRepository clientRepository, Publisher publisher) {
        this.clientRepository = clientRepository;
        this.publisher = publisher;
    }

    public ClientResponse createClient(ClientRequest request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setAge(request.getAge());
        client.setBirthDate(request.getBirthDate());

        Client savedClient = clientRepository.save(client);

        publisher.publishEmailEvent(createAlertEmail(client));

        return modelMapper.map(savedClient, ClientResponse.class);
    }

    private EmailEvent createAlertEmail(Client client) {
        String subject = "Alta de cliente" + client.getFirstName();

        String body = String.format(
                "<h1>¡Fue dado de alta un nuevo cliente!</h1>" +
                        "<p>Detalles:</p>" +
                        "<ul>" +
                        "<li>Nombre completo: %s %s</li>" +
                        "<li>Fecha de registro: %s</li>" +
                        "</ul>",
                client.getFirstName(),
                client.getLastName(),
                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        );

        return new EmailEvent(
                email,
                subject,
                body,
                true
        );
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

    public MetricsResponse getClientMetrics() {
        List<Integer> ages = clientRepository.findAllAges();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        ages.forEach(stats::addValue);

        MetricsResponse response = new MetricsResponse();
        response.setAverageAge(stats.getN() > 0 ? stats.getMean() : 0.0);
        response.setStandardDeviation(stats.getN() > 0 ? stats.getStandardDeviation() : 0.0);
        response.setTotalClients(stats.getN());

        return response;
    }
}
