package com.example.phase4.dto.response;

import com.example.phase4.entity.enums.Role;

import java.time.LocalDate;

public record FilteredUserResponseDTO(Role role,
                                      Long id,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      LocalDate signUpDate) {
}
