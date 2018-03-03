package com.google.developer.udacityalumni.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseActivity;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.developer.udacityalumni.user.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authStateListener;
    @BindView(R.id.sign_in_button)
    SignInButton signInBtn;

    public static Intent getLaunchIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressBar = findViewById(R.id.progressBar);
        authStateListener = firebaseAuth -> addFirebaseUser();
    }

    @OnClick(R.id.sign_in_button)
    public void signInButtonClicked() {
        signIn();
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, buildGoogleSignInOptions())
                .build();
    }

    private GoogleSignInOptions buildGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(buildGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data);
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            progressBar.setVisibility(View.GONE);
            showErrorSnackbar();
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential);
    }

    private void addFirebaseUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Uri uri = user.getPhotoUrl();
            String photoUrl = uri != null ? uri.toString() : null;
            FirebaseFirestore.getInstance().collection(CollectionNames.USERS)
                    .document(user.getUid())
                    .set(new User(user.getUid(), user.getDisplayName(), user.getEmail(), photoUrl))
                    .addOnSuccessListener(aVoid -> finish())
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        showErrorSnackbar();
                        e.printStackTrace();
                    });
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        progressBar.setVisibility(View.GONE);
        showSnackbar(R.id.sign_in_layout, R.string.login_error_connecting_google_play_services);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private void showErrorSnackbar() {
        showSnackbar(R.id.sign_in_layout, R.string.login_failed);
    }
}

