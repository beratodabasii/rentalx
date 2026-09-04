package com.rentalx.rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRentalRequest {
    @NotNull
    private Long reservationId;
    @NotNull
    @PositiveOrZero
    private Integer pickupKilometer;
}
