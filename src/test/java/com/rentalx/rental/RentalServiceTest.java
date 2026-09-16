package com.rentalx.rental;

import com.rentalx.enums.RentalStatus;
import com.rentalx.enums.ReservationStatus;
import com.rentalx.enums.VehicleStatus;
import com.rentalx.exception.InvalidRentalRequestException;
import com.rentalx.exception.RentalConflictException;
import com.rentalx.rental.dto.CreateRentalRequest;
import com.rentalx.rental.dto.CreateRentalResponse;
import com.rentalx.rental.dto.ReturnRentalRequest;
import com.rentalx.rental.entity.Rental;
import com.rentalx.rental.repository.RentalRepository;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import com.rentalx.user.repository.UserRepository;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.rentalx.rental.service.RentalService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void shouldCreateRentalSuccessfully() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);
        vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setVehicle(vehicle);

        CreateRentalRequest request = new CreateRentalRequest();
        request.setReservationId(1L);
        request.setPickupKilometer(20000);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(rentalRepository.existsByReservationId(1L))
                .thenReturn(false);

        when(rentalRepository.save(any(Rental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateRentalResponse response =
                rentalService.createRental(request);

        assertEquals(RentalStatus.ACTIVE, response.getStatus());
        assertEquals(VehicleStatus.RENTED, vehicle.getVehicleStatus());

        verify(rentalRepository).save(any(Rental.class));
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void shouldThrowExceptionWhenReservationIsNotConfirmed() {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);

        CreateRentalRequest request = new CreateRentalRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThrows(RentalConflictException.class, () ->
                rentalService.createRental(request)
        );

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRentalAlreadyExists() {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);

        CreateRentalRequest request = new CreateRentalRequest();
        request.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(rentalRepository.existsByReservationId(1L))
                .thenReturn(true);

        assertThrows(RentalConflictException.class, () ->
                rentalService.createRental(request)
        );

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldReturnRentalSuccessfully() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);
        vehicle.setVehicleStatus(VehicleStatus.RENTED);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setVehicle(vehicle);
        reservation.setEndDateTime(LocalDateTime.now().plusDays(1));

        Rental rental = Rental.builder()
                .reservation(reservation)
                .pickupKilometer(20000)
                .pickupDateTime(LocalDateTime.now().minusDays(1))
                .status(RentalStatus.ACTIVE)
                .lateFee(BigDecimal.ZERO)
                .build();

        ReturnRentalRequest request = new ReturnRentalRequest();
        request.setRentalId(1L);
        request.setReturnKilometer(20500);

        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        when(rentalRepository.save(any(Rental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateRentalResponse response =
                rentalService.returnRental(request);

        assertEquals(RentalStatus.RETURNED, response.getStatus());
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getVehicleStatus());
        assertEquals(BigDecimal.ZERO, response.getLateFee());

        verify(reservationRepository).save(reservation);
        verify(vehicleRepository).save(vehicle);
        verify(rentalRepository).save(rental);
    }

    @Test
    void shouldThrowExceptionWhenReturnKilometerIsLowerThanPickupKilometer() {
        Rental rental = Rental.builder()
                .pickupKilometer(20000)
                .status(RentalStatus.ACTIVE)
                .build();

        ReturnRentalRequest request = new ReturnRentalRequest();
        request.setRentalId(1L);
        request.setReturnKilometer(19000);

        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        assertThrows(InvalidRentalRequestException.class, () ->
                rentalService.returnRental(request)
        );

        verify(rentalRepository, never()).save(any());
    }
}