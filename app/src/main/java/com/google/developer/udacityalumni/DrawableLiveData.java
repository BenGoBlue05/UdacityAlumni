package com.google.developer.udacityalumni;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.google.developer.udacityalumni.networking.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Created by benjaminlewis on 2/17/18.
 */

public class DrawableLiveData extends LiveData<Resource<Drawable>> {

    private final URL url;

    public DrawableLiveData(URL url) {
        this.url = url;
        if (url == null) {
            setValue(Resource.error(null));
        } else {
            setValue(Resource.loading(null));
            loadData();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return Drawable.createFromStream(input, null);
                } catch (IOException e) {
                    Timber.e("IOException downloading drawable from url");
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                setValue(drawable != null ? Resource.success(drawable) : Resource.error(null));
            }
        }.execute();
    }
}
