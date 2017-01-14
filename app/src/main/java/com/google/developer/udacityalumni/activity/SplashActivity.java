package com.google.developer.udacityalumni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *
 * Created by Tom Calver on 12/01/17. <a href="https://github.com/PPartisan">GitHub/PPartisan</a>
 *
 */

public final class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread timer = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }

        };
        timer.start();
    }


}
