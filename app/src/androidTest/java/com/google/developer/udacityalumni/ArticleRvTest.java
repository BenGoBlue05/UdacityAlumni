package com.google.developer.udacityalumni;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.developer.udacityalumni.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ArticleRvTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void verifyRecyclerViewItems(){
//       TODO Verify Items in adapter
//        https://medium.com/google-developers/adapterviews-and-espresso-f4172aa853cf#.zgw3053d1
//        https://google.github.io/android-testing-support-library/docs/espresso/cheatsheet/index.html

    }

}
