package com.example.phase4.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record OfferResponseDTO(Long id,
                               Long orderId,
                               String specialistFirstName,
                               String specialistLastName,
                               int specialistScore,
                               LocalDate offerDate,
                               Double specialistPrice,
                               LocalDate startDate,
                               LocalTime startTime,
                               LocalTime finishTime,
                               Boolean isAccepted) {
}
