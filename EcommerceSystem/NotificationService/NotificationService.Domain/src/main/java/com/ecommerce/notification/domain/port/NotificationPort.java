package com.ecommerce.notification.domain.port;

public interface NotificationPort {
    void sendEmail(String to, String subject, String body);
}
