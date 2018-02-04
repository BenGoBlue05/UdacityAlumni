package com.google.developer.udacityalumni.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.animation.BottomNavigationViewBehavior;
import com.google.developer.udacityalumni.apps.AppsFragment;
import com.google.developer.udacityalumni.base.BaseActivity;
import com.google.developer.udacityalumni.login.LoginActivity;
import com.google.developer.udacityalumni.post.NewPostActivity;
import com.google.developer.udacityalumni.post.PostsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private PostsFragment postsFragment;

    private AppsFragment appsFragment;

    private NewsFragment newsFragment;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        if (savedInstanceState == null) {
            postsFragment = PostsFragment.newInstance();
            addFragment(R.id.fragmentContainer, postsFragment);
        }
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            goToLoginScreen();
        }
    }

    private void goToLoginScreen() {
        startActivity(LoginActivity.getLaunchIntent(this));
    }

    private void setUpViews() {
        setSupportActionBar(findViewById(R.id.toolbar));
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewPostActivity.class)));
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        bottomNavigationView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = postsFragment;
                fab.show();
                break;
            case R.id.navigation_apps:
                if (appsFragment == null) {
                    appsFragment = AppsFragment.newInstance();
                }
                fab.hide();
                fragment = appsFragment;
                break;
            case R.id.navigation_news:
                if (newsFragment == null) {
                    newsFragment = NewsFragment.newInstance();
                }
                fab.hide();
                fragment = newsFragment;
                break;
            default:
                return false;
        }
        replaceFragment(R.id.fragmentContainer, fragment);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(aVoid -> goToLoginScreen())
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
