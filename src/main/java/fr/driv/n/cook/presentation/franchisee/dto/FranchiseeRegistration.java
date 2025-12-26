package fr.driv.n.cook.presentation.franchisee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FranchiseeRegistration(
        @NotBlank @Email @Size(max = 320) String email,
        @NotBlank @Size(min = 8, max = 255) String password,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @Size(max = 30) String phone,
        @NotBlank @Size(max = 150) String companyName,
        @NotBlank @Size(max = 255) String address
) {
}
