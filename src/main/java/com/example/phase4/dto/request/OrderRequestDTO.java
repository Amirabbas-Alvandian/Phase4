package com.example.phase4.dto.request;

import com.example.phase4.dto.AddressDTO;

import java.time.LocalDate;

public record OrderRequestDTO(String customerEmail,
                              Double customerPrice,
                              String description,
                              LocalDate startDate,
                              AddressDTO addressDTO) {
}
