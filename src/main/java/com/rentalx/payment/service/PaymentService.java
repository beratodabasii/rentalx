package com.rentalx.payment.service;

import com.rentalx.enums.PaymentStatus;
import com.rentalx.enums.ReservationStatus;
import com.rentalx.exception.ForbiddenOperationException;
import com.rentalx.exception.PaymentConflictException;
import com.rentalx.exception.ReservationNotFoundException;
import com.rentalx.payment.dto.CreatePaymentRequest;
import com.rentalx.payment.dto.CreatePaymentResponse;
import com.rentalx.payment.entity.Payment;
import com.rentalx.payment.repository.PaymentRepository;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    public PaymentService(PaymentRepository paymentRepository,  ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest createPaymentRequest , String userEmail) {

        Reservation reservation = reservationRepository.findById(createPaymentRequest.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));


        if(!reservation.getUser().getEmail().equals(userEmail)) {
            throw new ForbiddenOperationException("You are not allowed to pay this reservation");
        }

        if (reservation.getExpiresAt().isBefore(LocalDateTime.now())) {


            throw new PaymentConflictException("Reservation payment period has expired");
        }

        if(!reservation.getStatus().equals(ReservationStatus.PENDING_PAYMENT)){

            throw new PaymentConflictException("Reservation is not awaiting payment");

        }
        boolean hasSuccessfulPayment = paymentRepository.existsByReservationIdAndStatus(reservation.getId(), PaymentStatus.SUCCESS);
        if(hasSuccessfulPayment) {

                throw new PaymentConflictException("Payment already exists");

        }

        long paymentCount = paymentRepository.countByReservationId(reservation.getId());
        int attemptNumber = Math.toIntExact(paymentCount + 1);

        if(attemptNumber>3){
            throw new PaymentConflictException("Maximum payment attempt limit reached");
        }

        PaymentStatus paymentStatus;
        if(createPaymentRequest.getSimulateSuccess()){
            paymentStatus = PaymentStatus.SUCCESS;
        }else {
            paymentStatus = PaymentStatus.FAILED;
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(reservation.getTotalPrice())
                .status(paymentStatus)
                .paymentType(createPaymentRequest.getPaymentType())
                .attemptNumber(attemptNumber)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        if(paymentStatus.equals(PaymentStatus.SUCCESS)){
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        }
        CreatePaymentResponse createPaymentResponse = new CreatePaymentResponse();
        createPaymentResponse.setPaymentId(savedPayment.getId());
        createPaymentResponse.setReservationId(reservation.getId());
        createPaymentResponse.setAmount(savedPayment.getAmount());
        createPaymentResponse.setPaymentType(savedPayment.getPaymentType());
        createPaymentResponse.setStatus(savedPayment.getStatus());
        createPaymentResponse.setAttemptNumber(savedPayment.getAttemptNumber());
        createPaymentResponse.setCreatedAt(savedPayment.getCreatedAt());
        return createPaymentResponse;

    }

}
