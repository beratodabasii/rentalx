package com.rentalx.rental.service;

import com.rentalx.enums.RentalStatus;
import com.rentalx.enums.ReservationStatus;
import com.rentalx.enums.VehicleStatus;
import com.rentalx.exception.*;
import com.rentalx.rental.dto.CreateRentalRequest;
import com.rentalx.rental.dto.CreateRentalResponse;
import com.rentalx.rental.dto.ReturnRentalRequest;
import com.rentalx.rental.entity.Rental;
import com.rentalx.rental.repository.RentalRepository;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import com.rentalx.user.entity.User;
import com.rentalx.user.repository.UserRepository;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    public RentalService(RentalRepository rentalRepository,  ReservationRepository reservationRepository,
                         VehicleRepository vehicleRepository , UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateRentalResponse createRental(CreateRentalRequest createRentalRequest) {
        Reservation reservation = reservationRepository.findById(createRentalRequest.getReservationId())
                .orElseThrow(()-> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getStatus().equals(ReservationStatus.CONFIRMED)){
            throw new RentalConflictException("Reservation cannot be started in its current status");
        }

        boolean rentalAlreadyExists = rentalRepository.existsByReservationId(createRentalRequest.getReservationId());
        if (rentalAlreadyExists){
            throw new RentalConflictException("Rental already exists for this reservation");

        }
        Vehicle vehicle = reservation.getVehicle();
        if (!vehicle.getVehicleStatus().equals(VehicleStatus.AVAILABLE)) {
            throw new RentalConflictException("Vehicle is not available for rental");
        }

        Rental rental = Rental.builder()
                .reservation(reservation)
                .pickupKilometer(createRentalRequest.getPickupKilometer())
                .pickupDateTime(LocalDateTime.now())
                .status(RentalStatus.ACTIVE)
                .lateFee(BigDecimal.ZERO)
                .build();
        Rental savedRental = rentalRepository.save(rental);
        vehicle.setVehicleStatus(VehicleStatus.RENTED);
        vehicleRepository.save(vehicle);

        CreateRentalResponse createRentalResponse = new CreateRentalResponse();
        createRentalResponse.setRentalId(savedRental.getId());
        createRentalResponse.setReservationId(reservation.getId());
        createRentalResponse.setVehicleId(vehicle.getId());
        createRentalResponse.setStatus(savedRental.getStatus());
        createRentalResponse.setPickupDateTime(savedRental.getPickupDateTime());
        createRentalResponse.setPickupKilometer(savedRental.getPickupKilometer());
        createRentalResponse.setLateFee(savedRental.getLateFee());
        createRentalResponse.setCreatedAt(savedRental.getCreatedAt());
        return createRentalResponse;

    }
    @Transactional
    public CreateRentalResponse returnRental(ReturnRentalRequest returnRentalRequest) {
        Rental rental = rentalRepository.findById(returnRentalRequest.getRentalId())
                .orElseThrow(()-> new RentalNotFoundException("Rental not found"));

        if(!rental.getStatus().equals(RentalStatus.ACTIVE)){
            throw new RentalConflictException("Rental is not active");
        }

        if (returnRentalRequest.getReturnKilometer() < rental.getPickupKilometer()) {
            throw new InvalidRentalRequestException("Return kilometer cannot be less than pickup kilometer");
        }

        rental.setReturnKilometer(returnRentalRequest.getReturnKilometer());
        rental.setReturnDateTime(LocalDateTime.now());
        rental.setStatus(RentalStatus.RETURNED);

        Reservation reservation = rental.getReservation();
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);
        Vehicle vehicle = reservation.getVehicle();
        vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        LocalDateTime returnDateTime = rental.getReturnDateTime();
        LocalDateTime expectedReturnDateTime = reservation.getEndDateTime();

        if(returnDateTime.isAfter(expectedReturnDateTime)){
            long lateDays = ChronoUnit.DAYS.between(expectedReturnDateTime,returnDateTime);
            if(lateDays == 0){
                lateDays = 1;
            }
            BigDecimal lateFee = BigDecimal.valueOf(lateDays).multiply(BigDecimal.valueOf(500));
            rental.setLateFee(lateFee);
        } else {
            rental.setLateFee(BigDecimal.ZERO);
        }
        Rental savedRental = rentalRepository.save(rental);
        CreateRentalResponse createRentalResponse = new CreateRentalResponse();
        createRentalResponse.setRentalId(savedRental.getId());
        createRentalResponse.setReservationId(reservation.getId());
        createRentalResponse.setVehicleId(vehicle.getId());
        createRentalResponse.setStatus(savedRental.getStatus());
        createRentalResponse.setPickupDateTime(savedRental.getPickupDateTime());
        createRentalResponse.setPickupKilometer(savedRental.getPickupKilometer());
        createRentalResponse.setLateFee(savedRental.getLateFee());
        createRentalResponse.setCreatedAt(savedRental.getCreatedAt());
        createRentalResponse.setReturnDateTime(savedRental.getReturnDateTime());
        createRentalResponse.setReturnKilometer(savedRental.getReturnKilometer());
        return createRentalResponse;

    }

    public CreateRentalResponse getRentalById(Long rentalId){
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(()-> new RentalNotFoundException("Rental not found"));

        CreateRentalResponse createRentalResponse = new CreateRentalResponse();
        createRentalResponse.setRentalId(rental.getId());
        createRentalResponse.setReservationId(rental.getReservation().getId());
        createRentalResponse.setVehicleId(rental.getReservation().getVehicle().getId());
        createRentalResponse.setStatus(rental.getStatus());
        createRentalResponse.setPickupDateTime(rental.getPickupDateTime());
        createRentalResponse.setPickupKilometer(rental.getPickupKilometer());
        createRentalResponse.setLateFee(rental.getLateFee());
        createRentalResponse.setCreatedAt(rental.getCreatedAt());
        createRentalResponse.setReturnDateTime(rental.getReturnDateTime());
        createRentalResponse.setReturnKilometer(rental.getReturnKilometer());
        return createRentalResponse;


    }

    public List<CreateRentalResponse> getActiveRentals(){
        List<Rental> activeRentals = rentalRepository.findByStatus(RentalStatus.ACTIVE);
        List<CreateRentalResponse> createRentalResponseList = new ArrayList<>();
        for (Rental rental : activeRentals) {
            CreateRentalResponse createRentalResponse = new CreateRentalResponse();
            createRentalResponse.setRentalId(rental.getId());
            createRentalResponse.setReservationId(rental.getReservation().getId());
            createRentalResponse.setVehicleId(rental.getReservation().getVehicle().getId());
            createRentalResponse.setStatus(rental.getStatus());
            createRentalResponse.setPickupDateTime(rental.getPickupDateTime());
            createRentalResponse.setPickupKilometer(rental.getPickupKilometer());
            createRentalResponse.setLateFee(rental.getLateFee());
            createRentalResponse.setCreatedAt(rental.getCreatedAt());
            createRentalResponse.setReturnDateTime(rental.getReturnDateTime());
            createRentalResponse.setReturnKilometer(rental.getReturnKilometer());
            createRentalResponseList.add(createRentalResponse);
        }
        return createRentalResponseList;

    }

    public List<CreateRentalResponse> getMyRentals(String email){

        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );

        List<Rental> rentals = rentalRepository.findByReservationUserId(user.getId());
        List<CreateRentalResponse> createRentalResponseList = new ArrayList<>();
        for (Rental rental : rentals) {
            CreateRentalResponse createRentalResponse = new CreateRentalResponse();
            createRentalResponse.setRentalId(rental.getId());
            createRentalResponse.setReservationId(rental.getReservation().getId());
            createRentalResponse.setVehicleId(rental.getReservation().getVehicle().getId());
            createRentalResponse.setStatus(rental.getStatus());
            createRentalResponse.setPickupDateTime(rental.getPickupDateTime());
            createRentalResponse.setPickupKilometer(rental.getPickupKilometer());
            createRentalResponse.setLateFee(rental.getLateFee());
            createRentalResponse.setCreatedAt(rental.getCreatedAt());
            createRentalResponse.setReturnDateTime(rental.getReturnDateTime());
            createRentalResponse.setReturnKilometer(rental.getReturnKilometer());
            createRentalResponseList.add(createRentalResponse);
        }
        return createRentalResponseList;

    }




}
