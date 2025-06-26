package com.pinApp.customerManagement.config;

import com.pinApp.customerManagement.model.dto.ClientRequest;
import com.pinApp.customerManagement.model.dto.ClientResponse;
import com.pinApp.customerManagement.model.dto.MetricsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
        name = "Clientes API",
        description = "API para gestión completa de clientes incluyendo creación, consulta y análisis de métricas"
)
public interface IClientSwagger {
    @Operation(
            summary = "Crear nuevo cliente",
            description = "Registra un nuevo cliente en el sistema con validación de datos",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClientRequest.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                            "id": 1,
                            "firstName": "Juan",
                            "lastName": "Pérez",
                            "age": 32,
                            "birthDate": "1993-05-15"
                        }
                        """
                            )
                    )
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "Cliente creado exitosamente",
            content = @Content(
                    schema = @Schema(implementation = ClientResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "id": 1,
                        "firstName": "Juan",
                        "lastName": "Pérez",
                        "age": 32,
                        "birthDate": "1993-05-15"
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                    {
                        "timestamp": "2025-06-23T22:21:48.5025844",
                        "status": 400,
                        "error": "Validation Error",
                        "message": "Error en la validación de los datos de entrada",
                        "path": "/clients",
                        "fieldErrors": {
                            "firstName": "El nombre es obligatorio"
                        }
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
    )
    ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request);

    @Operation(
            summary = "Obtener todos los clientes",
            description = "Retorna una lista paginada de todos los clientes registrados con sus datos completos, incluyendo cálculo de expectativa de vida basado en su fecha de nacimiento",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de clientes obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class)),
                    examples = @ExampleObject(
                            value = """
                [
                    {
                        "id": 1,
                        "firstName": "Juan",
                        "lastName": "Pérez",
                        "age": 30,
                        "birthDate": "1993-05-15",
                        "estimatedDeathDate": "2073-05-15"
                    },
                    {
                        "id": 2,
                        "firstName": "Maria",
                        "lastName": "Garcia",
                        "age": 25,
                        "birthDate": "1998-10-22",
                        "estimatedDeathDate": "2078-10-22"
                    }
                ]
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
    )
    ResponseEntity<List<ClientResponse>> getAllClients();

    @Operation(
            summary = "Obtener métricas avanzadas de clientes",
            description = "Calcula y retorna estadísticas detalladas sobre la base de clientes",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Métricas calculadas exitosamente",
            content = @Content(
                    schema = @Schema(implementation = MetricsResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "averageAge": 32.0,
                        "standardDeviation": 0.0,
                        "totalClients": 5
                    }
                    """
                    )
            )
    )
    @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    ResponseEntity<MetricsResponse> getClientMetrics();

}
