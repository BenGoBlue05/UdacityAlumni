package com.google.developer.udacityalumni.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import com.google.developer.udacityalumni.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Tom Calver on 23/01/17.
 */

public final class NavMenuServiceConnection extends CustomTabsServiceConnection {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({CLASSROOM,CATALOG,SUCCESS})
    public @interface Urls{}
    public static final String CLASSROOM = "https://classroom.udacity.com/me";
    public static final String CATALOG = "https://www.udacity.com/courses/all";
    public static final String SUCCESS = "https://www.udacity.com/success";

    private static final String TAB_PACKAGE_NAME = "com.android.chrome";

    private final WeakReference<Activity> mActivity;
    private CustomTabsIntent mCustomTabsIntent;

    public NavMenuServiceConnection(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public void launchUrl(@Urls String url) {
        final Context context = mActivity.get();
        if(mCustomTabsIntent != null && context != null) {
            setReferralUri(context, mCustomTabsIntent.intent);
            mCustomTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }

    /**
     * Call to begin pre-loading the pages defined in {@link Urls} (i.e. in
     * {@link Activity#onStart()})
     */
    public void bindService() {
        final Context context = mActivity.get();
        if (context != null) {
            CustomTabsClient.bindCustomTabsService(context, TAB_PACKAGE_NAME, this);
        }
    }

    public void unbindService() {
        final Context context = mActivity.get();
        if(context != null) {
            context.unbindService(this);
        }
    }

    private void setReferralUri(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            final String referrer = Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName();
            intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse(referrer));
        }
    }

    private CustomTabsIntent buildCustomTabsIntent(CustomTabsSession session) {
        final Context context = mActivity.get();
        if(context == null) return null;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.WHITE);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(
                context, android.R.anim.slide_in_left, android.R.anim.slide_out_right
        );
        builder.setCloseButtonIcon(getTintedBackArrowBitmap(context));
        return builder.build();
    }

    private Bitmap getTintedBackArrowBitmap(Context context) {
        VectorDrawableCompat drawable = VectorDrawableCompat.create(
                context.getResources(), R.drawable.ic_arrow_back, context.getTheme()
        );
        if (drawable == null) {
            throw new RuntimeException("Back indicator should never be null");
        }
        drawable.setTint(ContextCompat.getColor(context, R.color.colorAccent));

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
        if (client != null) {
            client.warmup(0);

            final List<Bundle> urls = new ArrayList<>(2);

            final Bundle catalog = new Bundle();
            catalog.putParcelable(CustomTabsService.KEY_URL, Uri.parse(CATALOG));

            final Bundle success = new Bundle();
            success.putParcelable(CustomTabsService.KEY_URL, Uri.parse(SUCCESS));

            urls.add(catalog);
            urls.add(success);

            final CustomTabsSession customTabSession = client.newSession(null);
            customTabSession.mayLaunchUrl(Uri.parse(CLASSROOM), null, urls);
            mCustomTabsIntent = buildCustomTabsIntent(customTabSession);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {}


}