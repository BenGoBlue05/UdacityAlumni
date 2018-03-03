package com.google.developer.udacityalumni.main;

import android.os.Bundle;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseActivity;
import com.google.developer.udacityalumni.login.LoginActivity;
import com.google.developer.udacityalumni.session.UASession;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MainFragment fragment = MainFragment.newInstance();
            addFragment(R.id.container, fragment);
            UASession.init(getApplication());
        }
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(LoginActivity.getLaunchIntent(this));
        }
    }

}
