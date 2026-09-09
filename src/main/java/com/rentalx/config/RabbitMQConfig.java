package com.rentalx.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue("payment.success.queue");
    }

    @Bean
    public DirectExchange rentalxExchange() {
        return new DirectExchange("rentalx.exchange");
    }

    @Bean
    public Binding paymentSuccessBinding(
            Queue paymentSuccessQueue,
            DirectExchange rentalxExchange
    ) {
        return BindingBuilder
                .bind(paymentSuccessQueue)
                .to(rentalxExchange)
                .with("payment.success");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
