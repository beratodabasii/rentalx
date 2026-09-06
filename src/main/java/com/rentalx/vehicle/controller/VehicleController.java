package com.rentalx.vehicle.controller;

import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import com.rentalx.vehicle.dto.UpdateVehicleRequest;
import com.rentalx.vehicle.dto.UpdateVehicleStatusRequest;
import com.rentalx.vehicle.dto.VehicleRequest;
import com.rentalx.vehicle.dto.VehicleResponse;
import com.rentalx.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public VehicleResponse createVehicle(@RequestBody @Valid VehicleRequest vehicleRequest ) {
        return vehicleService.createVehicle(vehicleRequest);
    }

    @GetMapping
    public Page<VehicleResponse> getAllVehicles(@RequestParam(required = false) VehicleStatus status,
                                                @RequestParam(required = false)VehicleType type
            , Pageable pageable) {

        return vehicleService.getAllVehicles(status,type,pageable);
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicleById(@PathVariable  Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public VehicleResponse updateVehicle(@PathVariable Long id, @Valid @RequestBody UpdateVehicleRequest updateVehicleRequest) {
        return vehicleService.updateVehicle(id, updateVehicleRequest);
    }

    @PatchMapping("/{id}/status")
    public VehicleResponse updateVehicleStatus(@PathVariable Long id, @Valid @RequestBody UpdateVehicleStatusRequest updateVehicleStatusRequest) {
        return vehicleService.updateVehicleStatus(id,updateVehicleStatusRequest);
    }

}
