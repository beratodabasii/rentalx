package com.rentalx.notification;

import com.rentalx.rabbitmq.event.PaymentSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendPaymentSuccessNotification(PaymentSuccessEvent paymentSuccessEvent) {
        System.out.println("Notification sent to " + paymentSuccessEvent.getUserEmail()
                + " for reservation " + paymentSuccessEvent.getReservationId()
                + " amount: " + paymentSuccessEvent.getAmount());
    }
}
