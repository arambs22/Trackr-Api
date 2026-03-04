package com.aram.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateListRequest(
        @NotBlank String name,
        @NotNull Integer position
) {}