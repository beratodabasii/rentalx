package com.rentalx.rental.dto;

import com.rentalx.enums.RentalStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class CreateRentalResponse {

    private Long rentalId;
    private Long reservationId;
    private Long vehicleId;
    private RentalStatus status;
    private LocalDateTime pickupDateTime;
    private Integer pickupKilometer;
    private BigDecimal lateFee;
    private LocalDateTime createdAt;
    private LocalDateTime returnDateTime;
    private Integer returnKilometer;

}
