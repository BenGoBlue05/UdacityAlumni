package com.google.developer.udacityalumni.activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


//credit Firebase Database example from official Firebase site
public class NewPostActivity extends BaseActivity {

    private static final String LOG_TAG = NewPostActivity.class.getSimpleName();

    private static final int RC_PHOTO = 100;
    private DatabaseReference mDb;
    private StorageReference mPhotoRef;
    private String mImageUrl;

    @BindView(R.id.new_post_image)
    ImageView mImageView;
    @BindView(R.id.new_post_et)
    EditText mEditText;


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
            close.setTint(ContextCompat.getColor(this, R.color.colorAccent));
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
        switch (item.getItemId()) {
            case R.id.post:
                addPost();
                startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                break;
            case R.id.item_post_image:
                addPhoto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addPost() {
        final String text;
        if (mEditText != null && mEditText.getText() != null && !mEditText.getText().toString().isEmpty()) {
            text = mEditText.getText().toString();
        } else {
            text = null;
        }
        if (text == null && mImageUrl == null) {
            Toast.makeText(this, getString(R.string.post_is_empty), Toast.LENGTH_LONG).show();
        } else {
            final String uId = getUid();
            mDb.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        Log.e(LOG_TAG, "USER IS NULL");
                        Toast.makeText(NewPostActivity.this, getString(R.string.user_null), Toast.LENGTH_LONG).show();
                    } else {
                        submitPost(uId, user.name, user.photoUrl, user.getBio(), text);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO);
    }

    private void submitPost(String userId, String userName, String userProfPic,
                            String userBio, String text) {
        if (mDb != null) {
            String key = mDb.child("posts").push().getKey();
            Post post = new Post(userId, userName, userProfPic, userBio, text, mImageUrl);
            Map<String, Object> postValues = post.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
            mDb.updateChildren(childUpdates);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Log.i(LOG_TAG, "IMAGE URI: " + imageUri);
                uploadImage(imageUri);
            }
//            if (mImageUrl != null) displayImage();
        }
    }




    private void displayImage(){
        mImageView.setVisibility(View.VISIBLE);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mPhotoRef)
                .into(mImageView);
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadImage(Uri uri){
        mPhotoRef = FirebaseStorage.getInstance().getReference().child("post_photos").child(uri.getLastPathSegment());
        mPhotoRef.putFile(uri)
                .addOnSuccessListener(NewPostActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageMetadata metadata = taskSnapshot.getMetadata();
                        mImageUrl = metadata != null && metadata.getDownloadUrl() != null ?
                                metadata.getDownloadUrl().toString() : null;
                        displayImage();
                    }
                })
                .addOnFailureListener(NewPostActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewPostActivity.this, "Upload failed",
                                Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "UPLOAD FAILED");
                    }
                });
    }
}

