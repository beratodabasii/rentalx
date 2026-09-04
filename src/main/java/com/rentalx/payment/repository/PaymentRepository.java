package com.rentalx.payment.repository;

import com.rentalx.enums.PaymentStatus;
import com.rentalx.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByReservationIdAndStatus(Long reservationId, PaymentStatus status);
    long countByReservationId(Long reservationId);
}
