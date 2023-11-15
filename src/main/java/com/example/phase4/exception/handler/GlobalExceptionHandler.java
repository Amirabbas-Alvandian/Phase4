package com.example.phase4.exception.handler;

import com.example.phase4.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicateEntityException.class)
    public ResponseEntity<String> duplicateExceptionHandler(DuplicateEntityException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<String> DataIntegrityExceptionHandler(DataIntegrityViolationException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = AttemptingToUpdateNonPersistentObjectException.class)
    public ResponseEntity<String> updateNotSavedEntityHandler(AttemptingToUpdateNonPersistentObjectException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = BadDateException.class)
    public ResponseEntity<String> BadDateExceptionExceptionHandler(BadDateException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = BadFormatException.class)
    public ResponseEntity<String> BadFormatExceptionHandler(BadFormatException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<String> EntityNotFoundExceptionHandler(EntityNotFoundException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = EntityNotSavedException.class)
    public ResponseEntity<String> EntityNotSavedExceptionHandler(EntityNotSavedException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = IllegalPriceException.class)
    public ResponseEntity<String> IllegalPriceExceptionHandler(IllegalPriceException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = InvalidAmountException.class)
    public ResponseEntity<String> InvalidAmountExceptionHandler(InvalidAmountException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = InvalidImageSizeException.class)
    public ResponseEntity<String> InvalidImageSizeExceptionHandler(InvalidImageSizeException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = NotEnoughBalanceException.class)
    public ResponseEntity<String> NotEnoughBalanceExceptionHandler(NotEnoughBalanceException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = SpecialistSubCategoryNotFoundException.class)
    public ResponseEntity<String> SpecialistSubCategoryNotFoundExceptionHandler(SpecialistSubCategoryNotFoundException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = WrongPathException.class)
    public ResponseEntity<String> WrongPathExceptionHandler(WrongPathException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = WrongUsernameOrPasswordException.class)
    public ResponseEntity<String> WrongUsernameOrPasswordExceptionHandler(WrongUsernameOrPasswordException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<String> NumberFormatExceptionHandler(NumberFormatException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> IOExceptionHandler(IOException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = UnRelatedOrderException.class)
    public ResponseEntity<String> UnRelatedOrderExceptionHandler(UnRelatedOrderException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = BadTimeException.class)
    public ResponseEntity<String> BadTimeExceptionHandler(BadTimeException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = NegativeSpecialistScoreException.class)
    public ResponseEntity<String> NegativeSpecialistScoreExceptionHandler(NegativeSpecialistScoreException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = SpecialistNotVerifiedException.class)
    public ResponseEntity<String> SpecialistNotVerifiedExceptionHandler(SpecialistNotVerifiedException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = InvalidCaptchaTextException.class)
    public ResponseEntity<String> InvalidCaptchaTextExceptionHandler(InvalidCaptchaTextException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(value = CaptchaRevokedException.class)
    public ResponseEntity<String> CaptchaRevokedExceptionHandler(CaptchaRevokedException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = AlreadySetFinishedException.class)
    public ResponseEntity<String> AlreadySetFinishedExceptionHandler(AlreadySetFinishedException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = AlreadyPayedException.class)
    public ResponseEntity<String> AlreadyPayedExceptionHandler(AlreadyPayedException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = UserAlreadyEnabledException.class)
    public ResponseEntity<String> UserAlreadyEnabledExceptionHandler(UserAlreadyEnabledException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = CustomerNotVerifiedException.class)
    public ResponseEntity<String> CustomerNotVerifiedExceptionHandler(CustomerNotVerifiedException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
