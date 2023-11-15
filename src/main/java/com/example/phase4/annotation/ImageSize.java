package com.example.phase4.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageSizeValidator.class)
public @interface ImageSize {

    public String message() default "size of file should be less than 300kb";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
