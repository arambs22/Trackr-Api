package com.aram.taskflow.dto;

import java.time.Instant;

public record BoardResponse(
        Long id,
        String name,
        Instant createdAt
) {}