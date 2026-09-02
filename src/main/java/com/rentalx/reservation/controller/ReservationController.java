package com.rentalx.reservation.controller;

import com.rentalx.reservation.dto.CreateReservationRequest;
import com.rentalx.reservation.dto.CreateReservationResponse;
import com.rentalx.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping
    public CreateReservationResponse createReservation(@RequestBody @Valid CreateReservationRequest createReservationRequest,
                                                       Authentication authentication) {
        String userEmail = authentication.getName();

        return reservationService.createReservation(createReservationRequest,  userEmail);

    }
    @GetMapping("/my")
    public List<CreateReservationResponse> getMyReservations( Authentication authentication) {
        String userEmail = authentication.getName();
        return reservationService.getMyReservations(userEmail);

    }
    @PatchMapping("/{id}/cancel")
    public CreateReservationResponse cancelReservation(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        return reservationService.cancelReservation(id, userEmail);

    }
}
