package com.aram.taskflow.dto;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Integer position
) {}