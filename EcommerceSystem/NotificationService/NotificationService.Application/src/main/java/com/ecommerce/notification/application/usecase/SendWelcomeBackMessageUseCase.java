package com.ecommerce.notification.application.usecase;

import com.ecommerce.shared.messaging.event.UserLoggedInEvent;

public interface SendWelcomeBackMessageUseCase {
    void execute(UserLoggedInEvent event);
}
