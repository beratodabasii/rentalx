package com.rentalx.reservation.scheduler;

import com.rentalx.enums.ReservationStatus;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationExpirationScheduler {

    private final ReservationRepository reservationRepository;
    public ReservationExpirationScheduler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @Scheduled(fixedDelay = 60000)
    public void expireReservations(){
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndExpiresAtBefore(ReservationStatus.PENDING_PAYMENT, LocalDateTime.now());

        for(Reservation reservation : expiredReservations){
            reservation.setStatus(ReservationStatus.EXPIRED);
        }
        reservationRepository.saveAll(expiredReservations);

    }

}
