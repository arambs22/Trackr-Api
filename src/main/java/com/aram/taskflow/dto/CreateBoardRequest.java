package com.aram.taskflow.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBoardRequest(
        @NotBlank String name
) {}