package com.google.developer.udacityalumni.notification;

import java.util.List;

/**
 * API for sending a Notification on new Spotlight articles. Create a new instance with
 * {@link SpotlightNotificationFactory}
 *
 * Created by Tom Calver on 11/01/17.
 *
 */

public interface SpotlightNotificationManager {

    void sendNotifications(List<SpotlightNotificationModel> models);

}
