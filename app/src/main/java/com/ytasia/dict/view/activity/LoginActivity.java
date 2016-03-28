package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import util.YTDictValues;
import ytasia.dictionary.R;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener {

    private Button Guest_SigninBt;
    private LoginButton Face_loginButton;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        YTDictValues.callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        //FacebookLogin();
        Face_loginButton = (LoginButton) findViewById(R.id.login_button);
        Face_loginButton.registerCallback(YTDictValues.callbackManager, new FacebookCallback<LoginResult>() {
            // if Login Success
            @Override
            public void onSuccess(final LoginResult loginResult) {
                YTDictValues.gUserid = AccessToken.getCurrentAccessToken().getUserId();
                //send to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            // If Login Cancel
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            // if Login Error
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }

        });

        // Login with Guest User
        Guest_SigninBt = (Button) findViewById(R.id.login_sign_in_button);
        Guest_SigninBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YTDictValues.guestid = "Guest_123";
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        YTDictValues.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        //SignInButton Setting
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    @Override
    public void onStart() {
        super.onStart();
        //Check Logged Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            AccessToken tok;
            tok = AccessToken.getCurrentAccessToken();
            YTDictValues.fUserid = tok.getUserId();
            //send to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //Check google logged
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(YTDictValues.mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        YTDictValues.callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //Sigin google display
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(YTDictValues.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information
            YTDictValues.gFullName = acct.getDisplayName();
            YTDictValues.gEmail = acct.getEmail();
            YTDictValues.gUserid = acct.getId();
            YTDictValues.isLogin = true;
            // Send Email/Name/Userid to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


}
