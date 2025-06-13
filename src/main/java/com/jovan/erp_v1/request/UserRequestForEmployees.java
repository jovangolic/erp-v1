package com.jovan.erp_v1.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UserRequestForEmployees(
                @NotBlank(message = "Ime je obavezno") String firstName,
                @NotBlank(message = "Prezime je obavezno") String lastName,
                @NotBlank(message = "Email je obavezan") String email,
                @NotBlank(message = "Broj telefona je obavezan") String phoneNumber,
                @NotBlank(message = "Adresa je obavezna") String address,
                @NotEmpty(message = "Korisnik mora imati najmanje jednu ulogu") Set<Long> roleIds) {
}
