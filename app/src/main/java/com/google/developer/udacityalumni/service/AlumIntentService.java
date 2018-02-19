package com.google.developer.udacityalumni.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.google.developer.udacityalumni.article.Article;
import com.google.developer.udacityalumni.article.Articles;
import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.notification.SpotlightNotificationFactory;
import com.google.developer.udacityalumni.notification.SpotlightNotificationManager;
import com.google.developer.udacityalumni.notification.SpotlightNotificationUtils;
import com.google.developer.udacityalumni.utils.Utility;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

public class AlumIntentService extends IntentService {

    private static final String LOG_TAG = AlumIntentService.class.getSimpleName();

    HashSet<Long> mSet;

    public AlumIntentService() {
        super(LOG_TAG);
    }

    public void addArticles(String json) {
        Articles articles = Utility.jsonStringToJavaObject(new TypeToken<Articles>() {}, json);
        Vector<ContentValues> articleCvVector = new Vector<>();
        for (int i = 0; i < articles.getArticles().size(); ++i) {
            Article article = articles.getArticles().get(i);
            if (!mSet.contains(article.getId())) {
                ContentValues values = new ContentValues();
                int isSpotlighted = article.getSpotlighted() ? 1 : 0;
                if (article.getTags().size() != 0) {
                    values.put(AlumContract.ArticleEntry.COL_RANDOM_TAG_ID, article.getTags().get(0).getId());
                    values.put(AlumContract.ArticleEntry.COL_RANDOM_TAG, article.getTags().get(0).getTag());
                }
                values.put(AlumContract.ArticleEntry.COL_ARTICLE_ID, article.getId());
                values.put(AlumContract.ArticleEntry.COL_TITLE, article.getTitle());
                values.put(AlumContract.ArticleEntry.COL_SPOTLIGHTED, isSpotlighted);
                values.put(AlumContract.ArticleEntry.COL_CONTENT, article.getContent());
                values.put(AlumContract.ArticleEntry.COL_IMAGE, article.getFeatureImage());
                values.put(AlumContract.ArticleEntry.COL_SLUG, article.getSlug());
                values.put(AlumContract.ArticleEntry.COL_USER_ID, article.getUser().getId());
                values.put(AlumContract.ArticleEntry.COL_USER_NAME, article.getUser().getName());
                if (article.getUser().getAvatar() != null) {
                    values.put(AlumContract.ArticleEntry.COL_USER_AVATAR, article.getUser().getAvatar());
                }
                values.put(AlumContract.ArticleEntry.COL_CREATED_AT, Utility.getTimeInMillis(article.getCreatedAt()));
                values.put(AlumContract.ArticleEntry.COL_UPDATED_AT, Utility.getTimeInMillis(article.getUpdatedAt()));
                values.put(AlumContract.ArticleEntry.COL_BOOKMARKED, 0);
                values.put(AlumContract.ArticleEntry.COL_FOLLOWING_AUTHOR, 0);
                articleCvVector.add(values);
            }
        }

        if (articleCvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[articleCvVector.size()];
            articleCvVector.toArray(cvArray);
            getContentResolver().bulkInsert(AlumContract.ArticleEntry.CONTENT_URI, cvArray);
            // Send notifications for any new Spotlighted articles
            Log.i(LOG_TAG, "Sending Notifications...");
            final SpotlightNotificationManager manager =
                    SpotlightNotificationFactory.newInstance(this);
            manager.sendNotifications(SpotlightNotificationUtils.toSpotlightedModels(cvArray));
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mSet = new HashSet<>();
        Cursor cursor = getContentResolver().query(AlumContract.ArticleEntry.CONTENT_URI,
                new String[]{String.valueOf(AlumContract.ArticleEntry.COL_ARTICLE_ID)}, null, null,
                AlumContract.ArticleEntry.COL_CREATED_AT + " DESC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                mSet.add(cursor.getLong(0));
            }
            cursor.close();
        }

        try {
            String json = Utility.fetch("http://udacity-alumni-api.herokuapp.com/api/v1/articles");
            if (json != null) addArticles(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}