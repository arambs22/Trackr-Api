package com.aram.taskflow.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBoardRequest(
        @NotBlank String name
) {}