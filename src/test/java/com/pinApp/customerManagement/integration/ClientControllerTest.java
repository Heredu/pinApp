package com.pinApp.customerManagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/clients";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Order(1)
    void getClientMetrics_EmptyDatabase_ReturnsZeroMetrics() throws Exception {
        mockMvc.perform(get(BASE_URL + "/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(0))
                .andExpect(jsonPath("$.standardDeviation").value(0))
                .andExpect(jsonPath("$.totalClients").value(0));
    }

    @Test
    void createClient_ValidRequest_ReturnsCreated() throws Exception {
        String requestJson = """
        {
            "firstName": "John",
            "lastName": "Doe",
            "age": 32,
            "birthDate": "1993-05-15"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void createClient_InvalidRequest_ReturnsBadRequest() throws Exception {
        String invalidRequestJson = """
        {
            "firstName": "",
            "lastName": "Doe",
            "age": -5,
            "birthDate": "2050-01-01"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.fieldErrors.firstName").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.fieldErrors.ageConsistentWithBirthDate").value("La edad debe coincidir con la fecha de nacimiento"))
                .andExpect(jsonPath("$.fieldErrors.age").value("La edad debe ser positiva"));
    }

    @Test
    void getAllClients_ReturnsListOfClients() throws Exception {
        // Primero creamos un cliente para tener datos
        createTestClient("John", "Doe", 32, LocalDate.of(1993, 5, 15));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].lifeExpectancyDate").exists());
    }

    @Test
    void getAllClients_EmptyDatabase_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getClientMetrics_WithClients_ReturnsMetrics() throws Exception {
        // Crear varios clientes para tener datos estadísticos
        createTestClient("John", "Doe", 32, LocalDate.of(1993, 5, 15));
        createTestClient("Jane", "Smith", 26, LocalDate.of(1998, 8, 20));
        createTestClient("Bob", "Johnson", 37, LocalDate.of(1988, 3, 10));

        mockMvc.perform(get(BASE_URL + "/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").isNumber())
                .andExpect(jsonPath("$.standardDeviation").isNumber())
                .andExpect(jsonPath("$.totalClients").value(3));
    }

    private void createTestClient(String firstName, String lastName, int age, LocalDate birthDate) throws Exception {
        String requestJson = String.format("""
        {
            "firstName": "%s",
            "lastName": "%s",
            "age": %d,
            "birthDate": "%s"
        }
        """, firstName, lastName, age, birthDate.toString());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
    }

}
