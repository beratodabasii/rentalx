package com.rentalx.vehicle.service;

import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import com.rentalx.exception.PlateNumberAlreadyExistsException;
import com.rentalx.exception.VehicleConflictException;
import com.rentalx.exception.VehicleNotFoundException;
import com.rentalx.vehicle.dto.UpdateVehicleRequest;
import com.rentalx.vehicle.dto.UpdateVehicleStatusRequest;
import com.rentalx.vehicle.dto.VehicleRequest;
import com.rentalx.vehicle.dto.VehicleResponse;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public VehicleResponse updateVehicle(Long vehicleId,UpdateVehicleRequest updateVehicleRequest) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()-> new VehicleNotFoundException("Vehicle not found"));


        boolean plateExists = vehicleRepository.existsByPlateNumberAndIdNot(
                updateVehicleRequest.getPlateNumber(),
                vehicle.getId()
        );
        if (plateExists) {
            throw new PlateNumberAlreadyExistsException("Plate number already exists");

        }


        vehicle.setBrand(updateVehicleRequest.getBrand());
        vehicle.setModel(updateVehicleRequest.getModel());
        vehicle.setYear(updateVehicleRequest.getYear());
        vehicle.setPlateNumber(updateVehicleRequest.getPlateNumber());
        vehicle.setVehicleType(updateVehicleRequest.getVehicleType());
        vehicle.setTransmissionType(updateVehicleRequest.getTransmissionType());
        vehicle.setFuelType(updateVehicleRequest.getFuelType());
        vehicle.setDailyPrice(updateVehicleRequest.getDailyPrice());
        vehicle.setKilometer(updateVehicleRequest.getKilometer());
        vehicle.setVehicleStatus(updateVehicleRequest.getVehicleStatus());

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

    public VehicleResponse updateVehicleStatus(Long vehicleId, UpdateVehicleStatusRequest updateVehicleStatusRequest) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

        if(vehicle.getVehicleStatus().equals(VehicleStatus.RENTED)){
            throw new VehicleConflictException("Rented vehicle status cannot be changed manually");
        }
        vehicle.setVehicleStatus(updateVehicleStatusRequest.getVehicleStatus());
        Vehicle savedVehicle =  vehicleRepository.save(vehicle);
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

    public Page<VehicleResponse> getAllVehicles(VehicleStatus status,
                                                VehicleType type, Pageable pageable) {
        Page<Vehicle> vehicles ;

        if(status != null && type != null) {
            vehicles = vehicleRepository.findByVehicleStatusAndVehicleType(status, type, pageable);
        } else if(status != null) {
            vehicles = vehicleRepository.findByVehicleStatus(status, pageable);
        }else if (type != null) {
            vehicles = vehicleRepository.findByVehicleType(type, pageable);

        } else {
            vehicles = vehicleRepository.findAll(pageable);
        }

        return vehicles.map(vehicle -> {
            VehicleResponse vehicleResponse = new VehicleResponse();
            vehicleResponse.setId(vehicle.getId());
            vehicleResponse.setBrand(vehicle.getBrand());
            vehicleResponse.setModel(vehicle.getModel());
            vehicleResponse.setYear(vehicle.getYear());
            vehicleResponse.setPlateNumber(vehicle.getPlateNumber());
            vehicleResponse.setVehicleType(vehicle.getVehicleType());
            vehicleResponse.setVehicleStatus(vehicle.getVehicleStatus());
            vehicleResponse.setTransmissionType(vehicle.getTransmissionType());
            vehicleResponse.setFuelType(vehicle.getFuelType());
            vehicleResponse.setDailyPrice(vehicle.getDailyPrice());
            vehicleResponse.setKilometer(vehicle.getKilometer());
            vehicleResponse.setCreatedAt(vehicle.getCreatedAt());
            vehicleResponse.setUpdatedAt(vehicle.getUpdatedAt());
            return vehicleResponse;
        });
    }

}
