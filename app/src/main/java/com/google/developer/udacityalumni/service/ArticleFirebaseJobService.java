package com.google.developer.udacityalumni.service;

import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ArticleFirebaseJobService extends JobService {
    private static final String LOG_TAG = ArticleFirebaseJobService.class.getSimpleName();
    public ArticleFirebaseJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(getApplicationContext(), AlumIntentService.class));
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e(LOG_TAG, "ARTICLE JOB INTERRUPTED");
        return true;
    }


}
