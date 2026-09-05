package com.rentalx.payment.dto;

import com.rentalx.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull
    private Long reservationId;
    @NotNull
    private PaymentType paymentType;
    @NotNull
    private Boolean simulateSuccess;
}
