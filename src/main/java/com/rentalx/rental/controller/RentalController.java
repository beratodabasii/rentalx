package com.rentalx.rental.controller;

import com.rentalx.rental.dto.CreateRentalRequest;
import com.rentalx.rental.dto.CreateRentalResponse;
import com.rentalx.rental.dto.ReturnRentalRequest;
import com.rentalx.rental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {
    private final RentalService rentalService;
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public CreateRentalResponse createRental(@Valid @RequestBody CreateRentalRequest createRequest) {
        return rentalService.createRental(createRequest);
    }

    @PatchMapping("/return")
    public CreateRentalResponse returnRental(@Valid @RequestBody ReturnRentalRequest returnRequest) {
        return rentalService.returnRental(returnRequest);
    }
}
