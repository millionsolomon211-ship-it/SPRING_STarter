package com.ecommerce.notification.infrastructure.messaging.listener;

import com.ecommerce.notification.application.usecase.SendWelcomeBackMessageUseCase;
import com.ecommerce.notification.application.usecase.SendWelcomeMessageUseCase;
import com.ecommerce.notification.infrastructure.messaging.NotificationLogStore;
import com.ecommerce.shared.messaging.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final MessageConverter messageConverter;
    private final SendWelcomeMessageUseCase welcomeUseCase;
    private final SendWelcomeBackMessageUseCase welcomeBackUseCase;

    @RabbitListener(queues = "notification.queue")
    public void onMessage(Message message) {
        try {
            Object event = messageConverter.fromMessage(message);
            String eventName = event.getClass().getSimpleName();
            log.info("NOTIFICATION RECEIVED: {} - Content: {}", eventName, event);
            addLog(eventName, event.toString());

            if (event instanceof UserRegisteredEvent e) {
                welcomeUseCase.execute(e);
            } else if (event instanceof UserLoggedInEvent e) {
                welcomeBackUseCase.execute(e);
            } else if (event instanceof OrderCreatedEvent e) {
                log.info("Order Alert: Order {} has been placed.", e.getOrderId());
            } else if (event instanceof PaymentCompletedEvent e) {
                log.info("Payment Alert: Payment successful for order {}", e.getOrderId());
            } else if (event instanceof PaymentFailedEvent e) {
                log.info("Payment Alert: Payment FAILED for order {}", e.getOrderId());
            } else if (event instanceof StockReservedEvent e) {
                log.info("Inventory Alert: Stock reserved for order {}", e.getOrderId());
            } else if (event instanceof StockFailedEvent e) {
                log.info("Inventory Alert: Stock FAILED for order {}", e.getOrderId());
            } else if (event instanceof ShipmentCreatedEvent e) {
                log.info("Shipping Alert: Shipment created for order {}", e.getOrderId());
            } else {
                log.warn("Unknown event received: {}", eventName);
            }
        } catch (Exception ex) {
            log.error("Failed to process notification message: {}", ex.getMessage(), ex);
        }
    }

    private void addLog(String event, String details) {
        String entry = String.format("🔔 [%s] %s: %s", LocalDateTime.now(), event, details);
        NotificationLogStore.NOTIFICATION_LOGS.add(entry);
    }
}
