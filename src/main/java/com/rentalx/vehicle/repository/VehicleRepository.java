package com.rentalx.vehicle.repository;

import com.rentalx.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

boolean existsByPlateNumber(String plateNumber);

}
