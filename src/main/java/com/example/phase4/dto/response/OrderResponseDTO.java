package com.example.phase4.dto.response;

import com.example.phase4.dto.AddressDTO;

import java.time.LocalDate;

public record OrderResponseDTO(Long id,
                               CustomerResponseDTO customerResponseDTO,
                               Double customerPrice,
                               String description,
                               LocalDate startDate,
                               LocalDate orderDate,
                               AddressDTO addressDTO) {
}
