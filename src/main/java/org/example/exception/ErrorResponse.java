package org.example.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message) { }
