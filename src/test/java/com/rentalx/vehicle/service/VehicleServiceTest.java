package com.rentalx.vehicle.service;

import com.rentalx.enums.VehicleStatus;
import com.rentalx.exception.VehicleConflictException;
import com.rentalx.exception.VehicleNotFoundException;
import com.rentalx.vehicle.VehicleService;
import com.rentalx.vehicle.dto.UpdateVehicleStatusRequest;
import com.rentalx.vehicle.dto.VehicleResponse;
import com.rentalx.vehicle.entity.Vehicle;
import com.rentalx.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void shouldThrowExceptionWhenUpdatingRentedVehicleStatus() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(4L);
        vehicle.setVehicleStatus(VehicleStatus.RENTED);
        when(vehicleRepository.findById(4L))
                .thenReturn(Optional.of(vehicle));

        UpdateVehicleStatusRequest updateVehicleStatusRequest = new UpdateVehicleStatusRequest();
        updateVehicleStatusRequest.setVehicleStatus(VehicleStatus.AVAILABLE);
        assertThrows(VehicleConflictException.class, () -> {
            vehicleService.updateVehicleStatus(4L, updateVehicleStatusRequest);
        });
    }

    @Test
    void shouldUpdateVehicleStatusSuccessfully(){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);
        vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
        UpdateVehicleStatusRequest updateVehicleStatusRequest = new UpdateVehicleStatusRequest();
        updateVehicleStatusRequest.setVehicleStatus(VehicleStatus.MAINTENANCE);
        when(vehicleRepository.findById(5L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        VehicleResponse response =
                vehicleService.updateVehicleStatus(5L, updateVehicleStatusRequest);
        assertEquals(VehicleStatus.MAINTENANCE, response.getVehicleStatus());
        verify(vehicleRepository).save(vehicle);

    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        UpdateVehicleStatusRequest updateVehicleStatusRequest =
                new UpdateVehicleStatusRequest();

        updateVehicleStatusRequest.setVehicleStatus(VehicleStatus.MAINTENANCE);

        when(vehicleRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.updateVehicleStatus(99L, updateVehicleStatusRequest);
        });
    }

}
