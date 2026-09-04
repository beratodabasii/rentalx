package com.rentalx.payment.dto;

import com.rentalx.enums.PaymentStatus;
import com.rentalx.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreatePaymentResponse {

    private Long paymentId;
    private Long reservationId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentType paymentType;
    private Integer attemptNumber;
    private LocalDateTime createdAt;
}
