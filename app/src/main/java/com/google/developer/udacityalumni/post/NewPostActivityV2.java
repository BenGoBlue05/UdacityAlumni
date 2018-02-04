package com.google.developer.udacityalumni.post;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.developer.udacityalumni.base.BaseActivity;

/**
 * Created by benjaminlewis on 1/10/18.
 */

public class NewPostActivityV2 extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(NewPostFragment.newInstance());
    }
}
