package com.example.phase4.dto.response;

import com.example.phase4.entity.enums.SpecialistStatus;

import java.time.LocalDate;

public record SpecialistRegisterResponseDTO(Long id,
                                            String firstName,
                                            String lastName,
                                            String email,
                                            LocalDate signUpDate,
                                            SpecialistStatus status//,
                                            //byte[] image
) {
}
