package com.google.developer.udacityalumni.fakedata;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.app.App;
import com.google.developer.udacityalumni.base.BaseFragment;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.developer.udacityalumni.data.JsonReader;
import com.google.developer.udacityalumni.post.Post;
import com.google.developer.udacityalumni.user.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FakeDataFragment extends BaseFragment {

    private final AtomicInteger usersUploaded = new AtomicInteger(0);
    private final AtomicInteger postsUploaded = new AtomicInteger(0);
    private final AtomicInteger appsUploaded = new AtomicInteger(0);
    private int totalUsers;
    private int totalPosts;
    private int totalApps;
    private boolean hasError;

    public FakeDataFragment() {
        // Required empty public constructor
    }

    public static FakeDataFragment newInstance() {

        Bundle args = new Bundle();

        FakeDataFragment fragment = new FakeDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fake_data, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            totalUsers = 0;
            totalPosts = 0;
            totalApps = 0;
            addUsers();
            addPosts();
            addApps();
        }
    }

    private void addApps() {
        List<App> apps = getModels(R.raw.apps, new TypeToken<ArrayList<App>>() {
        }.getType());
        if (apps != null) {
            totalApps = apps.size();
            for (App app : apps) {
                FirebaseFirestore.getInstance()
                        .collection(CollectionNames.APPS)
                        .document(app.getId())
                        .set(app)
                        .addOnCompleteListener(task -> {
                            appsUploaded.incrementAndGet();
                            handleResponse(task);
                        });
            }
        }
    }

    private void addUsers() {
        List<User> users = getModels(R.raw.users, new TypeToken<ArrayList<User>>() {
        }.getType());
        if (users != null) {
            totalUsers = users.size();
            for (User user : users) {
                FirebaseFirestore.getInstance()
                        .collection(CollectionNames.USERS)
                        .document(user.getId())
                        .set(user)
                        .addOnCompleteListener(task -> {
                            usersUploaded.incrementAndGet();
                            handleResponse(task);
                        });
            }
        }
    }

    private void handleResponse(Task<?> task) {
        if (!task.isSuccessful()) hasError = true;
        if (isUploadsFinished()) {
            if (!hasError) updateSharedPreferences();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    private void addPosts() {
        List<Post> posts = getModels(R.raw.posts, new TypeToken<ArrayList<Post>>() {
        }.getType());
        if (posts != null) {
            totalPosts = posts.size();
            for (Post post : posts) {
                FirebaseFirestore.getInstance().collection(CollectionNames.POSTS)
                        .document(post.getId())
                        .set(post)
                        .addOnCompleteListener(task -> {
                            postsUploaded.incrementAndGet();
                            handleResponse(task);
                        });
            }
        }
    }

    private <T> T getModels(@RawRes int id, Type type) {
        InputStream inputStream = getResources().openRawResource(id);
        try {
            return JsonReader.readJsonStream(inputStream, type);
        } catch (IOException e) {
            Timber.e("Failure reading raws JSON");
            return null;
        }
    }

    private boolean isUploadsFinished() {
        return usersUploaded.get() == totalUsers && postsUploaded.get() == totalPosts;
    }

    private void updateSharedPreferences() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    .edit().putBoolean(getString(R.string.fake_data_key), true).apply();
        }
    }

}
