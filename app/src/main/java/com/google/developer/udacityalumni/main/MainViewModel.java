package com.google.developer.udacityalumni.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.developer.udacityalumni.DrawableLiveData;
import com.google.developer.udacityalumni.networking.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by benjaminlewis on 2/15/18.
 */

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> navState = new MutableLiveData<>();

    private DrawableLiveData profPic;

    public MainViewModel() {
        profPic = new DrawableLiveData(getProfPicUrl());
        navState.setValue(MainFragment.HOME);
    }

    public LiveData<Integer> getNavState() {
        return navState;
    }

    public void setNavState(@MainFragment.NavState int state) {
        if (navState.getValue() != null && navState.getValue() != state) {
            navState.setValue(state);
        }
    }

    private URL getProfPicUrl() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Uri uri = user.getPhotoUrl();
            if (uri != null) try {
                return new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public LiveData<Resource<Drawable>> getCurrentUserProfPic() {
        return profPic;
    }
}
