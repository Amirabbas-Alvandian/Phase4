package com.example.phase4.dto;

import jakarta.validation.constraints.Pattern;

public record CardDto(
                      Integer captchaId,
                      String captcha,
                      @Pattern(regexp = "[0-9]{16}",message = "caredNumber -> only 16 numbers")
                      String cardNumber,
                      @Pattern(regexp = "[0-9]{3}",message = "cvv2 -> only 3 numbers")
                      String cvv2,
                      @Pattern(regexp = "[0-9]{6}",message = "secondPassword -> only 6 numbers")
                      String secondPassword,
                      long orderId) {
}
