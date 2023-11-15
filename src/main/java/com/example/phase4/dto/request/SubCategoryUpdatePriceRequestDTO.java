package com.example.phase4.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record SubCategoryUpdatePriceRequestDTO(
        @Pattern(regexp = "[A-Za-z]{3,}"

                ,message = "category name should at least contain 3 letters")
        String categoryName,


        @Pattern(regexp = "[A-Za-z]{3,}"

                ,message = "subCategory name should at least contain 3 letters")
        String subCategoryName,

        @Min(value = 1, message = "base price should be more than zero")
        double price) {
}
