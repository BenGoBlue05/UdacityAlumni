package com.google.developer.udacityalumni.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.activity.MainActivity;

import java.util.List;

final class SpotlightNotificationManagerImpl implements SpotlightNotificationManager {

    private static final String GROUP_KEY_ARTICLES = "group_key_articles";

    private final Context mContext;
    private final NotificationManagerCompat mManager;

    SpotlightNotificationManagerImpl(Context context) {
        mContext = context;
        mManager = NotificationManagerCompat.from(mContext);
    }

    @Override
    public void sendNotifications(List<SpotlightNotificationModel> models) {

        final int count = models.size();

        if (count == 1) {
            notifySingle(models.get(0));
        } else if (count > 1) {
            notifyStacked(models);
        }

    }

    private void notifySingle(SpotlightNotificationModel model) {
        final NotificationCompat.Builder builder = buildBaseNotification(model);
        final NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(model.title);
        style.bigText(model.content);
        builder.setStyle(style);
        mManager.notify(model.getId(), builder.build());
    }

    private void notifyStacked(List<SpotlightNotificationModel> models) {

        final int count = models.size();
        final Resources res = mContext.getResources();

        final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setSummaryText(res.getQuantityString(R.plurals.spotlight_articles_title, count));
        style.setBigContentTitle(res.getString(R.string.notification_title));

        for (int i = 0; i < count - 1; i++) {
            final SpotlightNotificationModel model = models.get(i);
            style.addLine(model.title);
            mManager.notify(model.getId(), buildBaseNotification(model).build());
        }

        final SpotlightNotificationModel lastModel = models.get(count-1);
        final NotificationCompat.Builder builder =
                buildBaseNotification(lastModel);
        builder.setStyle(style);
        builder.setGroupSummary(true);
        mManager.notify(lastModel.getId(), builder.build());

    }

    private NotificationCompat.Builder buildBaseNotification(SpotlightNotificationModel model) {

        /*
        It's possible here to link to a specific article or page dependent on how "Spotlight" is
        implemented. For now it simply links to the main page.
         */
        final Intent loadArticleIntent = new Intent(mContext, MainActivity.class);
        loadArticleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pIntent = PendingIntent.getActivity(
                mContext, 0, loadArticleIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setGroup(GROUP_KEY_ARTICLES);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_fiber_new_white_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        builder.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        builder.setContentTitle(model.title);
        builder.setContentIntent(pIntent);

        return builder;

    }

}
