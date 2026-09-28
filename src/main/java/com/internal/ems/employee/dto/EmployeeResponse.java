package com.internal.ems.employee.dto;

import java.time.Instant;

public record EmployeeResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    Long departmentId,
    String departmentName,
    Instant createdAt,
    Instant updatedAt
) {}
