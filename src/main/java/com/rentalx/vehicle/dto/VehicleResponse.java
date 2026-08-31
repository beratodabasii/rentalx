package com.rentalx.vehicle.dto;

import com.rentalx.enums.FuelType;
import com.rentalx.enums.TransmissionType;
import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VehicleResponse {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String plateNumber;
    private VehicleType vehicleType;
    private TransmissionType transmissionType;
    private FuelType fuelType;
    private VehicleStatus vehicleStatus;
    private BigDecimal dailyPrice;
    private Integer kilometer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
