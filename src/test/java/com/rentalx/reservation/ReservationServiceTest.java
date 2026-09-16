package com.rentalx.reservation;

import com.rentalx.reservation.service.ReservationService;
import com.rentalx.enums.ReservationStatus;
import com.rentalx.exception.ForbiddenOperationException;
import com.rentalx.exception.InvalidReservationRequestException;
import com.rentalx.exception.ReservationConflictException;
import com.rentalx.exception.UserNotFoundException;
import com.rentalx.reservation.dto.CreateReservationRequest;
import com.rentalx.reservation.dto.CreateReservationResponse;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import com.rentalx.user.entity.User;
import com.rentalx.user.repository.UserRepository;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldCreateReservationSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);
        vehicle.setDailyPrice(BigDecimal.valueOf(1000));

        CreateReservationRequest request = new CreateReservationRequest();
        request.setVehicleId(5L);
        request.setStartDateTime(LocalDateTime.now().plusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(3));

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(vehicleRepository.findById(5L))
                .thenReturn(Optional.of(vehicle));

        when(reservationRepository
                .existsByVehicleIdAndStatusInAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                        anyLong(), anyList(), any(), any()
                ))
                .thenReturn(false);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateReservationResponse response =
                reservationService.createReservation(request, "user@test.com");

        assertEquals(ReservationStatus.PENDING_PAYMENT, response.getStatus());
        assertEquals(BigDecimal.valueOf(2000), response.getTotalPrice());

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenReservationDatesConflict() {
        User user = new User();
        user.setEmail("user@test.com");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);

        CreateReservationRequest request = new CreateReservationRequest();
        request.setVehicleId(5L);
        request.setStartDateTime(LocalDateTime.now().plusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(3));

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(vehicleRepository.findById(5L))
                .thenReturn(Optional.of(vehicle));

        when(reservationRepository
                .existsByVehicleIdAndStatusInAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                        anyLong(), anyList(), any(), any()
                ))
                .thenReturn(true);

        assertThrows(ReservationConflictException.class, () ->
                reservationService.createReservation(request, "user@test.com")
        );

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsInPast() {
        User user = new User();
        user.setEmail("user@test.com");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);

        CreateReservationRequest request = new CreateReservationRequest();
        request.setVehicleId(5L);
        request.setStartDateTime(LocalDateTime.now().minusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(1));

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(vehicleRepository.findById(5L))
                .thenReturn(Optional.of(vehicle));

        assertThrows(InvalidReservationRequestException.class, () ->
                reservationService.createReservation(request, "user@test.com")
        );

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        CreateReservationRequest request = new CreateReservationRequest();

        when(userRepository.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                reservationService.createReservation(request, "missing@test.com")
        );
    }

    @Test
    void shouldThrowExceptionWhenCancellingAnotherUsersReservation() {
        User owner = new User();
        owner.setEmail("owner@test.com");

        Reservation reservation = new Reservation();
        reservation.setUser(owner);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThrows(ForbiddenOperationException.class, () ->
                reservationService.cancelReservation(1L, "other@test.com")
        );

        verify(reservationRepository, never()).save(any());
    }
}