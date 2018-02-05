package com.google.developer.udacityalumni.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.developer.udacityalumni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public void addFragment(@IdRes int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    public void addFragment(Fragment fragment){
        addFragment(R.id.fragmentContainer, fragment);
    }

    public void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    public Snackbar getSnackBar(@IdRes int container, @StringRes int message) {
        return Snackbar.make(findViewById(container), message, Snackbar.LENGTH_LONG);
    }

    public Snackbar getSnackBar(@IdRes int container, String message) {
        return Snackbar.make(findViewById(container), message, Snackbar.LENGTH_LONG);
    }

    public void showSnackbar(@IdRes int container, @StringRes int message) {
        Snackbar snackbar = getSnackBar(container, message);
        if (snackbar != null){
            snackbar.show();
        }
    }

}
