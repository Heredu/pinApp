package com.pinApp.customerManagement.config;

import com.pinApp.customerManagement.controller.AuthController;
import com.pinApp.customerManagement.exception.ErrorResponse;
import com.pinApp.customerManagement.model.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticación", description = "Operaciones de autenticación y generación de tokens")
public interface IAuthSwagger {
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y genera tokens de acceso y refresco",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de autenticación",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                            "username": "admin",
                            "password": "admin123"
                        }
                        """
                            )
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthController.AuthResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "timestamp": "2025-06-24T00:33:36.1630536",
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Usuario o contraseña incorrectos",
                        "path": "/auth/login",
                        "fieldErrors": null
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "timestamp": "2023-10-05T12:00:00Z",
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "Error al procesar la autenticación"
                    }
                    """
                    )
            )
    )
    ResponseEntity<AuthController.AuthResponse> login(@RequestBody AuthRequest request);
}
