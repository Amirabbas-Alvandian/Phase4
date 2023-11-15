package com.example.phase4.dto.response;

public record RatingResponseDTO(long id,
                                long orderId,
                                String comment,
                                int score) {
}
