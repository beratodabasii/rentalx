package com.rentalx.rental.entity;

import com.rentalx.enums.RentalStatus;
import com.rentalx.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;
    @Column(nullable = false)
    private LocalDateTime pickupDateTime;
    private LocalDateTime returnDateTime;
    @Column(nullable = false)
    private Integer pickupKilometer;
    private Integer returnKilometer;
    @Column(nullable = false)
    private BigDecimal lateFee;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false , unique = true)
    private Reservation reservation;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
