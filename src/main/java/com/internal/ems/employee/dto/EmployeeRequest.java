package com.internal.ems.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
    @Schema(description = "Employee's first name", example = "John")
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    String firstName,

    @Schema(description = "Employee's last name", example = "Doe")
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    String lastName,

    @Schema(description = "Unique corporate email address", example = "john.doe@company.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    String email,

    @Schema(description = "Phone number in international E.164 format", example = "+919876543210")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid E.164 format (e.g. +919876543210)")
    String phone,

    @Schema(description = "Identifier of the department the employee belongs to", example = "1")
    @NotNull(message = "Department ID is required")
    Long departmentId
) {}
