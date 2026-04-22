package de.asxka.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;

public class NotificationUtils {

    /**
     * Sendet eine einfache LabyMod-Benachrichtigung (Notification) an den Spieler.
     *
     * @param title Der Titel der Notification
     * @param text  Der Text/Inhalt der Notification
     */
    public static void pushNotification(String title, String text) {
        Notification notification = Notification.builder()
                .title(Component.text(title))
                .text(Component.text(text))
                .build();

        Laby.labyAPI().notificationController().push(notification);
    }

    /**
     * Sendet eine Notification mit formatierbaren Komponenten (für Farben etc.).
     *
     * @param titleComponent Die Komponente für den Titel
     * @param textComponent  Die Komponente für den Text
     */
    public static void pushComponentNotification(Component titleComponent, Component textComponent) {
        Notification notification = Notification.builder()
                .title(titleComponent)
                .text(textComponent)
                .build();

        Laby.labyAPI().notificationController().push(notification);
    }
}

