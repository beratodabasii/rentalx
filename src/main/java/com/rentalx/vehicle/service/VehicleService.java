package com.rentalx.vehicle.service;

import com.rentalx.enums.VehicleStatus;
import com.rentalx.exception.PlateNumberAlreadyExistsException;
import com.rentalx.exception.VehicleNotFoundException;
import com.rentalx.vehicle.dto.VehicleRequest;
import com.rentalx.vehicle.dto.VehicleResponse;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {
        if(vehicleRepository.existsByPlateNumber(vehicleRequest.getPlateNumber())) {
            throw new PlateNumberAlreadyExistsException("Plate number already exists");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleRequest.getBrand());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setYear(vehicleRequest.getYear());
        vehicle.setPlateNumber(vehicleRequest.getPlateNumber());
        vehicle.setVehicleType(vehicleRequest.getVehicleType());
        vehicle.setTransmissionType(vehicleRequest.getTransmissionType());
        vehicle.setFuelType(vehicleRequest.getFuelType());
        vehicle.setDailyPrice(vehicleRequest.getDailyPrice());
        vehicle.setKilometer(vehicleRequest.getKilometer());
        vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(savedVehicle.getId());
        vehicleResponse.setBrand(savedVehicle.getBrand());
        vehicleResponse.setModel(savedVehicle.getModel());
        vehicleResponse.setYear(savedVehicle.getYear());
        vehicleResponse.setPlateNumber(savedVehicle.getPlateNumber());
        vehicleResponse.setVehicleType(savedVehicle.getVehicleType());
        vehicleResponse.setTransmissionType(savedVehicle.getTransmissionType());
        vehicleResponse.setFuelType(savedVehicle.getFuelType());
        vehicleResponse.setVehicleStatus(savedVehicle.getVehicleStatus());
        vehicleResponse.setDailyPrice(savedVehicle.getDailyPrice());
        vehicleResponse.setKilometer(savedVehicle.getKilometer());
        vehicleResponse.setCreatedAt(savedVehicle.getCreatedAt());
        vehicleResponse.setUpdatedAt(savedVehicle.getUpdatedAt());
        return vehicleResponse;


    }


    public List<VehicleResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleResponse> responses = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            VehicleResponse vehicleResponse = new VehicleResponse();

            vehicleResponse.setId(vehicle.getId());
            vehicleResponse.setBrand(vehicle.getBrand());
            vehicleResponse.setModel(vehicle.getModel());
            vehicleResponse.setYear(vehicle.getYear());
            vehicleResponse.setPlateNumber(vehicle.getPlateNumber());
            vehicleResponse.setVehicleType(vehicle.getVehicleType());
            vehicleResponse.setTransmissionType(vehicle.getTransmissionType());
            vehicleResponse.setFuelType(vehicle.getFuelType());
            vehicleResponse.setVehicleStatus(vehicle.getVehicleStatus());
            vehicleResponse.setDailyPrice(vehicle.getDailyPrice());
            vehicleResponse.setKilometer(vehicle.getKilometer());
            vehicleResponse.setCreatedAt(vehicle.getCreatedAt());
            vehicleResponse.setUpdatedAt(vehicle.getUpdatedAt());

            responses.add(vehicleResponse);

        }
        return responses;

    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(()-> new VehicleNotFoundException("Vehicle not found"));

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(vehicle.getId());
        vehicleResponse.setBrand(vehicle.getBrand());
        vehicleResponse.setModel(vehicle.getModel());
        vehicleResponse.setYear(vehicle.getYear());
        vehicleResponse.setPlateNumber(vehicle.getPlateNumber());
        vehicleResponse.setVehicleType(vehicle.getVehicleType());
        vehicleResponse.setTransmissionType(vehicle.getTransmissionType());
        vehicleResponse.setFuelType(vehicle.getFuelType());
        vehicleResponse.setVehicleStatus(vehicle.getVehicleStatus());
        vehicleResponse.setDailyPrice(vehicle.getDailyPrice());
        vehicleResponse.setKilometer(vehicle.getKilometer());
        vehicleResponse.setCreatedAt(vehicle.getCreatedAt());
        vehicleResponse.setUpdatedAt(vehicle.getUpdatedAt());
        return vehicleResponse;
    }

}
