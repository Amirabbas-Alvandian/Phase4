package com.example.phase4.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CategoryRequestDTO(
        @NotNull
        @Pattern(regexp = "[A-Za-z]{3,}"
                ,message = "category name should at least contain 3 letters")
        String name) {
}
