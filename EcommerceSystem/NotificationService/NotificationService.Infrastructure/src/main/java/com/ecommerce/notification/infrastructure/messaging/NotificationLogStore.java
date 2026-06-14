package com.ecommerce.notification.infrastructure.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class NotificationLogStore {
    public static final List<String> NOTIFICATION_LOGS = Collections.synchronizedList(new ArrayList<>());
}
