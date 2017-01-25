package com.google.developer.udacityalumni.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


//credit Firebase Database example from official Firebase site
public class NewPostActivity extends BaseActivity {

    private static final String LOG_TAG = NewPostActivity.class.getSimpleName();

    private DatabaseReference mDb;
    @BindView(R.id.new_post_image)
    ImageView mImage;
    @BindView(R.id.new_post_et)
    EditText mInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        ButterKnife.bind(this);
        mDb = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable close = ContextCompat.getDrawable(this, R.drawable.ic_close);
            close.setTint(ContextCompat.getColor(this, android.R.color.black));
            actionBar.setHomeAsUpIndicator(close);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.post:
                addPost();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.item_post_image:
//                TODO: Get photo from phone and store it in Fb Storage
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addPost(){
        final String text;
        final String image;
        if (mInput != null && mInput.getText() != null && !mInput.getText().toString().isEmpty()){
            text = mInput.getText().toString();
        } else{
            text = null;
        }

        if (mImage != null && mImage.getVisibility() == View.VISIBLE){
//            TODO: complete addPhoto()
            image = null; //Remove
        } else{
            image = null;
        }
        if (text == null && image == null) {
            Toast.makeText(this, getString(R.string.post_is_empty), Toast.LENGTH_LONG).show();
        } else{
            final String uId = getUid();
            mDb.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null){
                        Log.e(LOG_TAG, "USER IS NULL");
                        Toast.makeText(NewPostActivity.this, getString(R.string.user_null), Toast.LENGTH_LONG).show();
                    } else{
                        submitPost(uId, user.name, user.photoUrl, text, image);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void addPhoto(){
        //        TODO: get permission to access photos on phone then add to fb storage and add url to fb db
    }

    private void submitPost(String userId, String userName, String userProfPic, String text, String photoUrl){
        if (mDb != null){
            String key= mDb.child("posts").push().getKey();
            Post post = new Post(userId, userName, userProfPic, text, photoUrl);
            Map<String, Object> postValues = post.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/userName-posts/" + userId + "/" + key, postValues);

            mDb.updateChildren(childUpdates);
        }

    }




}

