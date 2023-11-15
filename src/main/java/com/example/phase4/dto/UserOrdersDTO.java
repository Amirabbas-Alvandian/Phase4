package com.example.phase4.dto;

import com.example.phase4.dto.response.CustomerResponseDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserOrdersDTO(Long offerId,
                            Double price,
                            LocalDate createDate,
                            LocalDate startDate,
                            LocalTime startTime,
                            LocalTime finishTime) {
}
