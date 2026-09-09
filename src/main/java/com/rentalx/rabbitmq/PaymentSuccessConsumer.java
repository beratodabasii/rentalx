package com.rentalx.rabbitmq;

import com.rentalx.notification.NotificationService;
import com.rentalx.rabbitmq.event.PaymentSuccessEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class PaymentSuccessConsumer {
    private final NotificationService notificationService;
    public PaymentSuccessConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @RabbitListener(queues = "payment.success.queue")
    public void consume(PaymentSuccessEvent event){

        notificationService.sendPaymentSuccessNotification(event);
    }
}
