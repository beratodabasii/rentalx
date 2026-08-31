package com.rentalx.vehicle.controller;

import com.rentalx.vehicle.dto.VehicleRequest;
import com.rentalx.vehicle.dto.VehicleResponse;
import com.rentalx.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<VehicleResponse> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicleById(@PathVariable  Long id) {
        return vehicleService.getVehicleById(id);
    }

}
