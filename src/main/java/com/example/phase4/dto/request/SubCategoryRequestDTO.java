package com.example.phase4.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record SubCategoryRequestDTO(
        @Pattern(regexp = "[A-Za-z]{3,}"
                ,message = "subCategory name should at least contain 3 letters")
        String name,
        @Min(value = 1, message = "base price should be more than zero")
        Double basePrice,

        @Pattern(regexp = "[A-Za-z]{3,}"
                ,message = "subCategory name should at least contain 3 letters")

        String description) {

}
