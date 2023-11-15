package com.example.phase4.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageTypeValidator implements ConstraintValidator<ImageType, MultipartFile> {

    Tika tika = new Tika();

    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext){
        try {
            return tika.detect(image.getBytes()).equals("image/jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}

