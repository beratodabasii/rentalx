package com.rentalx.exception;

public class PlateNumberAlreadyExistsException extends RuntimeException {
    public PlateNumberAlreadyExistsException(String message) {
        super(message);
    }
}
