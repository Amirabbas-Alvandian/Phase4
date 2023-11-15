package com.example.phase4.dto.response;

import java.time.LocalDate;


public record CustomerResponseDTO(Long id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  LocalDate signUpDate) {
}
