package com.aram.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateListRequest(
        @NotBlank String name,
        @NotNull Integer position
) {}