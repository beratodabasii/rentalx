package com.rentalx.reservation.service;

import com.rentalx.enums.ReservationStatus;
import com.rentalx.exception.*;
import com.rentalx.reservation.dto.CreateReservationRequest;
import com.rentalx.reservation.dto.CreateReservationResponse;
import com.rentalx.reservation.entity.Reservation;
import com.rentalx.reservation.repository.ReservationRepository;
import com.rentalx.user.entity.User;
import com.rentalx.user.repository.UserRepository;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    public ReservationService(ReservationRepository reservationRepository , VehicleRepository vehicleRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public CreateReservationResponse createReservation(CreateReservationRequest request , String userEmail)  {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(
                () -> new VehicleNotFoundException("Vehicle not found")
        );

      if(request.getStartDateTime().isBefore(LocalDateTime.now())) {
           throw new InvalidReservationRequestException("Start date cannot be before current date");
      }

      if (request.getStartDateTime().isAfter(request.getEndDateTime() ) || request.getStartDateTime().isEqual(request.getEndDateTime())) {
          throw new InvalidReservationRequestException("Start date must be before end date");
      }

        List<ReservationStatus> status = new ArrayList<>();
        status.add(ReservationStatus.PENDING_PAYMENT);
        status.add(ReservationStatus.CONFIRMED);

        boolean hasConflict = reservationRepository.existsByVehicleIdAndStatusInAndStartDateTimeLessThanAndEndDateTimeGreaterThan(request.getVehicleId(),
                status, request.getEndDateTime() ,request.getStartDateTime());


        if(hasConflict) {
            throw new ReservationConflictException("Vehicle is not available for the selected dates");
        }
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setVehicle(vehicle);
        reservation.setStartDateTime(request.getStartDateTime());
        reservation.setEndDateTime(request.getEndDateTime());

        Long rentalDays = ChronoUnit.DAYS.between(request.getStartDateTime(), request.getEndDateTime());
        BigDecimal dailyPrice = vehicle.getDailyPrice();
        BigDecimal totalPrice = dailyPrice.multiply(BigDecimal.valueOf(rentalDays));
        reservation.setDailyPrice(dailyPrice);
        reservation.setTotalPrice(totalPrice);

        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        Reservation savedReservation = reservationRepository.save(reservation);
        CreateReservationResponse response = new CreateReservationResponse();
        response.setReservationId(savedReservation.getId());
        response.setVehicleId(savedReservation.getVehicle().getId());
        response.setStartDateTime(savedReservation.getStartDateTime());
        response.setEndDateTime(savedReservation.getEndDateTime());
        response.setDailyPrice(savedReservation.getDailyPrice());
        response.setTotalPrice(savedReservation.getTotalPrice());
        response.setStatus(savedReservation.getStatus());
        response.setExpiresAt(savedReservation.getExpiresAt());
        response.setCreatedAt(savedReservation.getCreatedAt());
        return response;


    }

    public List<CreateReservationResponse> getMyReservations(String userEmail) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException ("User not found")
        );

        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        List<CreateReservationResponse> responses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            CreateReservationResponse response = new CreateReservationResponse();
            response.setReservationId(reservation.getId());
            response.setVehicleId(reservation.getVehicle().getId());
            response.setStartDateTime(reservation.getStartDateTime());
            response.setEndDateTime(reservation.getEndDateTime());
            response.setDailyPrice(reservation.getDailyPrice());
            response.setTotalPrice(reservation.getTotalPrice());
            response.setStatus(reservation.getStatus());
            response.setExpiresAt(reservation.getExpiresAt());
            response.setCreatedAt(reservation.getCreatedAt());
            responses.add(response);
        }
        return responses;
    }

    public CreateReservationResponse cancelReservation(Long reservationId, String userEmail){

      Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
              ()-> new ReservationNotFoundException("Reservation not found")
      );

      if(!reservation.getUser().getEmail().equals(userEmail)) {
            throw new ForbiddenOperationException("You are not allowed to cancel this reservation");
      }

      if(!reservation.getStatus().equals(ReservationStatus.PENDING_PAYMENT) &&  !reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
          throw new ReservationConflictException("Reservation cannot be cancelled in its current status");
      }
      reservation.setStatus(ReservationStatus.CANCELLED);
      Reservation savedReservation = reservationRepository.save(reservation);
      CreateReservationResponse response = new CreateReservationResponse();
      response.setReservationId(savedReservation.getId());
      response.setVehicleId(savedReservation.getVehicle().getId());
      response.setStartDateTime(savedReservation.getStartDateTime());
      response.setEndDateTime(savedReservation.getEndDateTime());
      response.setDailyPrice(savedReservation.getDailyPrice());
      response.setTotalPrice(savedReservation.getTotalPrice());
      response.setStatus(savedReservation.getStatus());
      response.setExpiresAt(savedReservation.getExpiresAt());
      response.setCreatedAt(savedReservation.getCreatedAt());
      return response;


    }

}
