package com.internal.ems.department.dto;

import java.time.Instant;

public record DepartmentResponse(
    Long id,
    String name,
    Instant createdAt,
    Instant updatedAt
) {}
