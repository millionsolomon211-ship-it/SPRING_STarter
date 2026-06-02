package com.ecommerce.notification.application.service;

import com.ecommerce.notification.application.usecase.SendWelcomeBackMessageUseCase;
import com.ecommerce.notification.application.usecase.SendWelcomeMessageUseCase;
import com.ecommerce.notification.domain.port.NotificationPort;
import com.ecommerce.shared.messaging.event.UserLoggedInEvent;
import com.ecommerce.shared.messaging.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationApplicationService implements SendWelcomeMessageUseCase, SendWelcomeBackMessageUseCase {

    private final NotificationPort notificationPort;

    @Override
    public void execute(UserRegisteredEvent event) {
        String body = String.format("Hello %s, welcome to our Ecommerce System!", event.getFullName());
        notificationPort.sendEmail(event.getEmail(), "Welcome!", body);
    }

    @Override
    public void execute(UserLoggedInEvent event) {
        String body = "Welcome back! We missed you.";
        notificationPort.sendEmail(event.getEmail(), "Welcome Back!", body);
    }
}
