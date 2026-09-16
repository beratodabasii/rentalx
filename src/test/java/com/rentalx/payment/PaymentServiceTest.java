package com.rentalx.payment;
import com.rentalx.payment.service.PaymentService;
import com.rentalx.enums.PaymentStatus;
import com.rentalx.enums.PaymentType;
import com.rentalx.enums.ReservationStatus;
import com.rentalx.exception.ForbiddenOperationException;
import com.rentalx.exception.PaymentConflictException;
import com.rentalx.payment.dto.CreatePaymentRequest;
import com.rentalx.payment.dto.CreatePaymentResponse;
import com.rentalx.payment.entity.Payment;
import com.rentalx.payment.repository.PaymentRepository;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import com.rentalx.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldCreateSuccessfulPayment() {
        User user = new User();
        user.setEmail("user@test.com");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        reservation.setTotalPrice(BigDecimal.valueOf(2500));

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setReservationId(1L);
        request.setPaymentType(PaymentType.CREDIT_CARD);
        request.setSimulateSuccess(true);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(paymentRepository.existsByReservationIdAndStatus(
                1L, PaymentStatus.SUCCESS))
                .thenReturn(false);

        when(paymentRepository.countByReservationId(1L))
                .thenReturn(0L);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreatePaymentResponse response =
                paymentService.createPayment(request, "user@test.com");

        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(1, response.getAttemptNumber());

        verify(reservationRepository).save(reservation);

        verify(rabbitTemplate).convertAndSend(
                eq("rentalx.exchange"),
                eq("payment.success"),
                any(Object.class)
        );
    }

    @Test
    void shouldThrowExceptionWhenPaymentPeriodExpired() {
        User user = new User();
        user.setEmail("user@test.com");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThrows(PaymentConflictException.class, () ->
                paymentService.createPayment(request, "user@test.com")
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPaymentAlreadyExists() {
        User user = new User();
        user.setEmail("user@test.com");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(paymentRepository.existsByReservationIdAndStatus(
                1L, PaymentStatus.SUCCESS))
                .thenReturn(true);

        assertThrows(PaymentConflictException.class, () ->
                paymentService.createPayment(request, "user@test.com")
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenMaximumPaymentAttemptReached() {
        User user = new User();
        user.setEmail("user@test.com");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(paymentRepository.existsByReservationIdAndStatus(
                1L, PaymentStatus.SUCCESS))
                .thenReturn(false);

        when(paymentRepository.countByReservationId(1L))
                .thenReturn(3L);

        assertThrows(PaymentConflictException.class, () ->
                paymentService.createPayment(request, "user@test.com")
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotOwnReservation() {
        User user = new User();
        user.setEmail("owner@test.com");

        Reservation reservation = new Reservation();
        reservation.setUser(user);

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThrows(ForbiddenOperationException.class, () ->
                paymentService.createPayment(request, "other@test.com")
        );
    }
}