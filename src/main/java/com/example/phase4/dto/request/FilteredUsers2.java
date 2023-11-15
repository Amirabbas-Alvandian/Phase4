package com.example.phase4.dto.request;

import com.example.phase4.entity.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record FilteredUsers2(LocalDate signUpDate,
                             @Positive
                             Integer count,
                             @Enumerated(EnumType.STRING)
                             Role role) {
}
