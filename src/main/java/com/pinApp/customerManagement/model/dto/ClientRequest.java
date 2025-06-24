package com.pinApp.customerManagement.model.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
@AllArgsConstructor
public class ClientRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotNull(message = "Se requiere edad")
    @Min(value = 0, message = "La edad debe ser positiva")
    private Integer age;

    @NotNull(message = "Se requiere fecha de nacimiento")
    private LocalDate birthDate;

    @AssertTrue(message = "La edad debe coincidir con la fecha de nacimiento")
    public boolean isAgeConsistentWithBirthDate() {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        return period.getYears() == age;
    }
}
