package com.rentalx.reservation.repository;

import com.rentalx.enums.ReservationStatus;
import com.rentalx.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByVehicleIdAndStatusInAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Long vehicleId,
            Collection<ReservationStatus> statuses,
            LocalDateTime endDateTime,
            LocalDateTime startDateTime
    );
    List<Reservation> findByUserId(Long userId);
    List<Reservation>  findByStatusAndExpiresAtBefore(ReservationStatus status,
                                                      LocalDateTime now);
}
