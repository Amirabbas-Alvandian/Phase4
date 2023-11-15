package com.example.phase4.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminRequestDTO(@NotBlank
                              @Pattern(regexp = "^[a-zA-Z]*$",message = "names should only contain alphabet")
                              String firstName,

                              @NotBlank
                              @Pattern(regexp = "^[a-zA-Z]*$",message = "names should only contain alphabet")
                              String lastName,
                              @Email
                              String email,
                              @NotBlank
                              @Pattern.List({
                                      @Pattern(regexp = "(?=.*[0-9]).+", message = "Password must contain one digit."),
                                      @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
                                      @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one upper letter."),
                                      @Pattern(regexp = "(?=.*[&%#@]).+", message ="Password must contain one special character."),
                                      @Pattern(regexp = "(?=\\S+$).+", message = "Password must contain no whitespace."),
                              })
                              String password) {
}
