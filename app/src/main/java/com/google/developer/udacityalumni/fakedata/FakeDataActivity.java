package com.google.developer.udacityalumni.fakedata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseActivity;

public class FakeDataActivity extends BaseActivity {

    Fragment fragment;

    public static Intent getLaunchIntent(@NonNull Context context) {
        return new Intent(context, FakeDataActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (fragment == null) {
            fragment = FakeDataFragment.newInstance();
        }
        addFragment(fragment);
    }
}
