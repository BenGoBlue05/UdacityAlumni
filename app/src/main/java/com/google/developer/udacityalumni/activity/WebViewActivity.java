package com.google.developer.udacityalumni.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.fragment.WebViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.success_stories));
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null && mToolbar != null) {
            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
            backArrow.setTint(ContextCompat.getColor(this, R.color.colorAccent));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }

        if (savedInstanceState == null) {
            String url = getIntent().getStringExtra(WebViewFragment.ARG_URL);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.web_view_container, WebViewFragment.newInstance(url))
                    .commit();
        }
    }
}
