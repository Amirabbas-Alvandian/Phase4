package com.example.phase4.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageTypeValidator.class)
public @interface ImageType {

    public String message() default "only jpg  format is accepted";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
