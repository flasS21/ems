package com.internal.ems.common.exception;

import java.time.Instant;

public record ErrorResponse(
    String code,
    String message,
    String path,
    Instant timestamp) {
}