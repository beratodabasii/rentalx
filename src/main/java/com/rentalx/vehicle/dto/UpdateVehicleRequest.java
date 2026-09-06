package com.rentalx.vehicle.dto;

import com.rentalx.enums.FuelType;
import com.rentalx.enums.TransmissionType;
import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateVehicleRequest {
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    private Integer year;
    @NotBlank
    private String plateNumber;
    @NotNull
    private VehicleType vehicleType;
    @NotNull
    private TransmissionType transmissionType;
    @NotNull
    private FuelType fuelType;
    @NotNull @Positive
    private BigDecimal dailyPrice;
    @NotNull @PositiveOrZero
    private Integer kilometer;
    @NotNull
    private VehicleStatus vehicleStatus;
}
