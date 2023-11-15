package com.example.phase4.dto.request;

import com.example.phase4.entity.enums.Role;
import com.example.phase4.entity.enums.SpecialistScoreMinMaxNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record FilteredUserRequestDTO(@Enumerated(EnumType.STRING)
                                     Role role,
                                     @Email
                                     String email,
                                     String firstName,
                                     String lastName,
                                     //@Positive
                                     long subCategoryId,
                                     @NotNull
                                     @Enumerated(EnumType.STRING)
                                     SpecialistScoreMinMaxNull specialistScore) {
}
