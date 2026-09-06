package com.rentalx.rental.repository;

import com.rentalx.enums.RentalStatus;
import com.rentalx.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByReservationId(Long reservationId);
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findByReservationUserId(Long userId);
}
