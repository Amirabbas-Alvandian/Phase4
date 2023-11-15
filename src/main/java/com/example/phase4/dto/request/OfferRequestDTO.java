package com.example.phase4.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record OfferRequestDTO(long specialistId,
                              double SpecialistPrice,
                              LocalDate startDate,
                              LocalTime startTime,

                              LocalTime finishTime,
                              long orderId) {
}
