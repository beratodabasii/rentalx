package com.rentalx.vehicle.dto;

import com.rentalx.enums.VehicleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleStatusRequest {
    @NotNull
    private VehicleStatus vehicleStatus;

}
