package com.rentalx.vehicle.repository;

import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import com.rentalx.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

boolean existsByPlateNumber(String plateNumber);
    boolean existsByPlateNumberAndIdNot(String plateNumber, Long id);
    Page<Vehicle> findByVehicleStatus(
            VehicleStatus vehicleStatus,
            Pageable pageable
    );
    Page<Vehicle> findByVehicleType(
            VehicleType vehicleType,
            Pageable pageable
    );
    Page<Vehicle> findByVehicleStatusAndVehicleType(
            VehicleStatus vehicleStatus,
            VehicleType vehicleType,
            Pageable pageable
    );

}
