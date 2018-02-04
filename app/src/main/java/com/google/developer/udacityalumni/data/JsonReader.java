package com.google.developer.udacityalumni.data;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Created by benjaminlewis on 1/26/18.
 */

public class JsonReader {

    private static final String TAG = JsonReader.class.getSimpleName();

    public static <T> T readJsonStream(InputStream inputStream, Type typeOfT) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonString = writer.toString();
        Gson gson = new Gson();
        return gson.fromJson(jsonString, typeOfT);
    }
}
