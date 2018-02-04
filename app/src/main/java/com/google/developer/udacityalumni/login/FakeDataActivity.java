package com.google.developer.udacityalumni.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.developer.udacityalumni.data.JsonReader;
import com.google.developer.udacityalumni.post.Post;
import com.google.developer.udacityalumni.user.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeDataActivity extends AppCompatActivity {

    private static final int ELON = 0;
    private static final int ZUCK = 1;
    private static final int THRUN = 2;
    private static final int PAGE = 3;
    private static final int GATES = 4;
    private static final int BEZOS = 5;

    private final static int TOTAL_USERS = 6;
    private final static int TOTAL_POSTS = 20;
    private final AtomicInteger usersUploaded = new AtomicInteger(0);
    private final AtomicInteger postsUploaded = new AtomicInteger(0);
    private boolean hasError;

    private static final String[] posts = {
            "Flying cars are the future.",
            "Automous cars are about to revolutionize society",
            "Android is the world's most popular OS",
            "Udacity is a great learning platform!",
            "iPhone X is SO expensive!",
            "Robots are taking over!",
            "AWS vs Azure vs Google Cloud is gonna be an epic battle",
            "The mobile web is the only web",
            "AI is the new electricity",
            "Voice assistants are the next OS platform frontier"
    };


    public static Intent getLaunchIntent(@NonNull Context context) {
        return new Intent(context, FakeDataActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            addFakeData();
        }
    }

    private void addFakeData() {
        addUsers();
        addPosts();
    }


    private void addUsers() {
        List<User> users = getUsers();
        for (User user : users) {
            FirebaseFirestore.getInstance().collection(CollectionNames.USERS).add(user)
                    .addOnCompleteListener(task -> {
                        usersUploaded.incrementAndGet();
                        handleResponse(task);
                    });
        }
    }

    private void handleResponse(Task<DocumentReference> task) {
        if (!task.isSuccessful()) hasError = true;
        if (isUploadsFinished()) {
            if (!hasError) updateSharedPreferences();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void addPosts() {
        for (int i = 0; i < TOTAL_POSTS; i++) {
            FirebaseFirestore.getInstance().collection(CollectionNames.POSTS).add(generateRandomPost())
                    .addOnCompleteListener(task -> {
                        postsUploaded.incrementAndGet();
                        handleResponse(task);
                    });
        }
    }

    private Post generateRandomPost() {
        Random random = new Random();
        int user = random.nextInt(6);
        return new Post(getUserId(user), getName(user), getProfPic(user), getRandomPostText());
    }

    private List<User> getUsers() {
        InputStream inputStream = getResources().openRawResource(R.raw.users);
        Type userTypeList = new TypeToken<ArrayList<User>>() {
        }.getType();
        try {
            return JsonReader.readJsonStream(inputStream, userTypeList);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private boolean isUploadsFinished() {
        return usersUploaded.get() == TOTAL_USERS && postsUploaded.get() == TOTAL_POSTS;
    }

    private void updateSharedPreferences() {
        getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .edit().putBoolean(getString(R.string.fake_data_key), true).apply();
    }

    private String getUserId(int user) {
        switch (user) {
            case ELON:
                return "fkjdaljfilf334";
            case ZUCK:
                return "HjiorehalHdiopaj283";
            case THRUN:
                return "klfdasljfkda";
            case PAGE:
                return "fljkhadlkhuresklhu432";
            case GATES:
                return "gfjdkslahjrl34lfgj";
            case BEZOS:
                return "rgjklhaklghrskjngro7";
            default:
                return "";
        }
    }

    private String getName(int user) {
        switch (user) {
            case ELON:
                return "Elon Musk";
            case ZUCK:
                return "Mark Zuckerberg";
            case THRUN:
                return "Sebastion Thrun";
            case PAGE:
                return "Larry Paige";
            case GATES:
                return "Bill Gates";
            case BEZOS:
                return "Jeff Bezos";
            default:
                return "";
        }
    }

    private String getProfPic(int user) {
        switch (user) {
            case ELON:
                return "https://cdn.dialogues.org/100555/images/5762298e7b2840353640e66e/tn_01wrsjo7uk_100555.jpg";
            case ZUCK:
                return "https://assets.change.org/photos/3/pz/ur/IZPZUrJczRxOpDB-128x128-noPad.jpg?1465599428";
            case THRUN:
                return "https://crunchbase-production-res.cloudinary.com/image/upload/c_thumb,h_256,w_256,f_auto,g_faces,z_0.7,q_auto:eco/v1477032915/mxnksvsveccaqgip2sib.png";
            case PAGE:
                return "http://shyrobotics.com/wp-content/uploads/2014/05/larry-page-128x128.jpg";
            case GATES:
                return "https://i.guim.co.uk/img/static/sys-images/Media/Pix/pictures/2006/06/21/GatesBillGetty1.jpg?w=300&q=55&auto=format&usm=12&fit=max&s=16f88f1b9f4451a65018572495dc2f51";
            case BEZOS:
                return "https://pbs.twimg.com/profile_images/669103856106668033/UF3cgUk4.jpg";
            default:
                return "";
        }
    }

    private String getRandomPostText() {
        return posts[new Random().nextInt(10)];
    }


}
