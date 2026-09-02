package com.rentalx.reservation.dto;

import com.rentalx.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationResponse {

    private Long reservationId;
    private Long vehicleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private BigDecimal dailyPrice;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

}
