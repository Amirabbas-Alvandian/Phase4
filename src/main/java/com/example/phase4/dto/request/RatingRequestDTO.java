package com.example.phase4.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record RatingRequestDTO(long orderId,
                               String comment,
                               @Positive(message = "score should be positive")
                                       @Max(value = 5,message = "max score -> 5")
                               int score
                               ) {
}
