package com.google.developer.udacityalumni.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by benjaminlewis on 2/15/18.
 */

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> navState = new MutableLiveData<>();

    public MainViewModel() {
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
}
