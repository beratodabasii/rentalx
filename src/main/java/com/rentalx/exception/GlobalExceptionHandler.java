package com.rentalx.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleInvalidCredentialsException(InvalidCredentialsException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors( MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        return ResponseEntity
                .badRequest()
                .body(errors);

    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleVehicleNotFoundException(VehicleNotFoundException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(PlateNumberAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handlePlateNumberAlreadyExistsException(PlateNumberAlreadyExistsException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<Map<String,String>> handleReservationConflictException(ReservationConflictException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}
