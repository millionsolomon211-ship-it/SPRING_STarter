package com.ecommerce.notification.application.usecase;

import com.ecommerce.shared.messaging.event.UserRegisteredEvent;

public interface SendWelcomeMessageUseCase {
    void execute(UserRegisteredEvent event);
}
