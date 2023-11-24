package com.example.unieval.ui.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.unieval.R;
import com.example.unieval.ui.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashscreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Logic for switching between group of users
                if (firebaseUser != null) {
                    // determine user category first
                    Intent i = new Intent(SplashscreenActivity.this, UserMainActivity.class);
                    startActivity(i);
                    //SplashScreenActivity.this.overridePendingTransition(0, 0);
                    finish();
                } else {
                    Intent i = new Intent(SplashscreenActivity.this, UserLoginActivity.class);
                    startActivity(i);
                    SplashscreenActivity.this.overridePendingTransition(0, 0);
                    finish();
                }
            }
        }, 3000);
    }
}