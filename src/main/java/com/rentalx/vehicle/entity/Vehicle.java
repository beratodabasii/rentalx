package com.rentalx.vehicle.entity;

import com.rentalx.enums.FuelType;
import com.rentalx.enums.TransmissionType;
import com.rentalx.enums.VehicleStatus;
import com.rentalx.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false , unique = true)
    private String plateNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmissionType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus vehicleStatus;
    @Column(nullable = false)
    private BigDecimal dailyPrice;
    @Column(nullable = false)
    private Integer kilometer;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
