package com.rentalx.rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnRentalRequest {
    @NotNull
    private Long rentalId;
    @NotNull
    @PositiveOrZero
    private Integer returnKilometer;

}
