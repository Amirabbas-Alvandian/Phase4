package com.example.phase4.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Embeddable
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Pattern(regexp = "[a-zA-Z]", message = "only alphabet allowed")
    private String province;

    @Pattern(regexp = "[a-zA-Z]", message = "only alphabet allowed")
    private String city;

    @Pattern(regexp = "[a-zA-Z0-9]", message = "Special characters not allowed")
    private String street;

    @Pattern(regexp = "[a-zA-Z0-9]", message = "Special characters not allowed")
    private String houseName;

    @Min(value = 1,message = "unit should be more than 1")
    private int unit;
}
