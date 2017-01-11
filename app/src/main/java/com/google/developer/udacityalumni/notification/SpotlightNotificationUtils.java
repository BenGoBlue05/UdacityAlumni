package com.google.developer.udacityalumni.notification;

import android.content.ContentValues;

import com.google.developer.udacityalumni.data.AlumContract.ArticleEntry;

import java.util.ArrayList;
import java.util.List;

public final class SpotlightNotificationUtils {

    private SpotlightNotificationUtils() { throw new AssertionError(); }

    public static List<SpotlightNotificationModel> toSpotlightedModels(ContentValues[] cvs) {

        if(cvs == null || cvs.length < 1) return new ArrayList<>();

        final int count = cvs.length;
        final List<SpotlightNotificationModel> models = new ArrayList<>(count);

        for (final ContentValues cv : cvs) {

            final boolean isSpotlighted = cv.getAsBoolean(ArticleEntry.COL_SPOTLIGHTED);

            if (isSpotlighted) {
                final String title = cv.getAsString(ArticleEntry.COL_TITLE);
                final String content = cv.getAsString(ArticleEntry.COL_CONTENT);
                final long id = cv.getAsLong(ArticleEntry.COL_ARTICLE_ID);

                final SpotlightNotificationModel.Builder builder =
                        new SpotlightNotificationModel.Builder();
                builder.setTitle(title).setContent(content).setId(id);
                models.add(builder.build());
            }
        }

        return models;

    }

}
