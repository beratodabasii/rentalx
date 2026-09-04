package com.rentalx.rental.repository;

import com.rentalx.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByReservationId(Long reservationId);
}
