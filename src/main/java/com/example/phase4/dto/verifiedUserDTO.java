package com.example.phase4.dto;

import java.time.LocalDate;

public record verifiedUserDTO (
                                Long id,
                                String firstName,
                                String lastName,
                                String email,
                                LocalDate signUpDate){

}
