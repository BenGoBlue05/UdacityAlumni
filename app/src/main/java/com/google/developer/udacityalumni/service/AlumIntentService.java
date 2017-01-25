package com.google.developer.udacityalumni.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.notification.SpotlightNotificationFactory;
import com.google.developer.udacityalumni.notification.SpotlightNotificationManager;
import com.google.developer.udacityalumni.notification.SpotlightNotificationUtils;
import com.google.developer.udacityalumni.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

public class AlumIntentService extends IntentService {

    private static final String LOG_TAG = AlumIntentService.class.getSimpleName();

    private static final String KEY_ARTICLES = "articles";
    private static final String KEY_ID = "id";
    private static final String KEY_SPOTLIGHTED = "spotlighted";
    private static final String KEY_USER = "user";
    private static final String KEY_TAGS = "tags";

    HashSet<Long> mSet;

    public AlumIntentService() {
        super(LOG_TAG);
    }

    public void addArticles(String json){
        try {
            JSONObject object = new JSONObject(json);
            JSONArray articles = object.getJSONArray(KEY_ARTICLES);
            int numArticles = articles != null ? articles.length() : 0;
            Vector<ContentValues> articleCvVector = new Vector<>();
            for (int i=0;i<numArticles;i++){
                JSONObject article = articles.getJSONObject(i);
                long articleId = article.getLong(KEY_ID);
                if (!mSet.contains(articleId)){
                    ContentValues values = new ContentValues();
                    int isSpotlighted = article.getBoolean(KEY_SPOTLIGHTED) ? 1 : 0;
                    JSONObject user = article.getJSONObject(KEY_USER);
                    JSONArray tags = article.getJSONArray(KEY_TAGS);
                    int ind = new Random().nextInt((tags.length() + 1));
                    JSONObject tag = tags.getJSONObject(ind);
                    values.put(AlumContract.ArticleEntry.COL_RANDOM_TAG_ID, tag.getLong("id"));
                    values.put(AlumContract.ArticleEntry.COL_RANDOM_TAG, tag.getString("tag"));
                    values.put(AlumContract.ArticleEntry.COL_ARTICLE_ID, articleId);
                    values.put(AlumContract.ArticleEntry.COL_TITLE, article.getString("title"));
                    values.put(AlumContract.ArticleEntry.COL_SPOTLIGHTED, isSpotlighted);
                    values.put(AlumContract.ArticleEntry.COL_CONTENT, article.getString("content"));
                    values.put(AlumContract.ArticleEntry.COL_IMAGE, article.getString("feature_image"));
                    values.put(AlumContract.ArticleEntry.COL_SLUG, article.getString("slug"));
                    values.put(AlumContract.ArticleEntry.COL_USER_ID, user.getLong("id"));
                    values.put(AlumContract.ArticleEntry.COL_USER_NAME, user.getString("name"));
                    if (user.get("avatar") != null)
                        values.put(AlumContract.ArticleEntry.COL_USER_AVATAR, user.getString("avatar"));
                    values.put(AlumContract.ArticleEntry.COL_CREATED_AT, Utility.getTimeInMillis(article.getString("created_at")));
                    values.put(AlumContract.ArticleEntry.COL_UPDATED_AT, Utility.getTimeInMillis(article.getString("updated_at")));
                    values.put(AlumContract.ArticleEntry.COL_BOOKMARKED, 0);
                    values.put(AlumContract.ArticleEntry.COL_FOLLOWING_AUTHOR, 0);
                    articleCvVector.add(values);
                }
            }

            if (articleCvVector.size() > 0){
                ContentValues[] cvArray = new ContentValues[articleCvVector.size()];
                articleCvVector.toArray(cvArray);
                getContentResolver().bulkInsert(AlumContract.ArticleEntry.CONTENT_URI, cvArray);
                // Send notifications for any new Spotlighted articles
                Log.i(LOG_TAG, "Sending Notifications...");
                final SpotlightNotificationManager manager =
                        SpotlightNotificationFactory.newInstance(this);
                manager.sendNotifications(SpotlightNotificationUtils.toSpotlightedModels(cvArray));
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
            while (cursor.moveToNext()){
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
