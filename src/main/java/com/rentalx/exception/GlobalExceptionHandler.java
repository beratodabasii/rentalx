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

    @ExceptionHandler(PaymentConflictException.class)
    public ResponseEntity<Map<String,String>> handlePaymentConflictException(PaymentConflictException exception){
        Map<String,String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRentalNotFoundException(
            RentalNotFoundException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(RentalConflictException.class)
    public ResponseEntity<Map<String, String>> handleRentalConflictException(
            RentalConflictException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(InvalidRentalRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRentalRequestException(
            InvalidRentalRequestException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReservationNotFoundException(
            ReservationNotFoundException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidReservationRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReservationRequestException(
            InvalidReservationRequestException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenOperationException(
            ForbiddenOperationException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException exception) {

        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(VehicleConflictException.class)
    public ResponseEntity<Map<String, String>> handleVehicleConflictException(
            VehicleConflictException exception
    ){
        Map<String, String> response = Map.of(
                "message", exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}
