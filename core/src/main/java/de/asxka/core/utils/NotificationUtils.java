package de.asxka.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;

public class NotificationUtils {


    public static void pushNotification(String title, String text) {
        Notification notification = Notification.builder()
                .title(Component.text(title))
                .text(Component.text(text))
                .build();

        Laby.labyAPI().notificationController().push(notification);
    }

    public static void pushComponentNotification(Component titleComponent, Component textComponent) {
        Notification notification = Notification.builder()
                .title(titleComponent)
                .text(textComponent)
                .build();

        Laby.labyAPI().notificationController().push(notification);
    }
}

