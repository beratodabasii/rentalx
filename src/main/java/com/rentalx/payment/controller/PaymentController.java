package com.rentalx.payment.controller;

import com.rentalx.payment.dto.CreatePaymentRequest;
import com.rentalx.payment.dto.CreatePaymentResponse;
import com.rentalx.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public CreatePaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest createPaymentRequest , Authentication authentication) {
        String userEmail = authentication.getName();
        return paymentService.createPayment(createPaymentRequest, userEmail);

    }

}
