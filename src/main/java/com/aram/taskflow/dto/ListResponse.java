package com.aram.taskflow.dto;

public record ListResponse(
        Long id,
        String name,
        Integer position
) {}