package com.example.unieval.ui.splashscreen;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.User;
import com.example.unieval.ui.admin.home.AdminMainActivity;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashscreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseUser firebaseUser;

    int themeMode;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Logic for switching between group of users
                if (firebaseUser != null) {

                    BaseRepository baseRepository = new BaseRepository();
                    baseRepository.getUser(firebaseUser.getUid()).observe(SplashscreenActivity.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            Intent i;
                            if (user.getRole().equals(Constants.ROLE_ADMIN)) {
                                i = new Intent(SplashscreenActivity.this, AdminMainActivity.class);
                                i.putExtra(Constants.KEY_SESSION_TYPE, user.getSessionType());
                            } else {
                                i = new Intent(SplashscreenActivity.this, UserMainActivity.class);
                                i.putExtra(Constants.KEY_USER_TYPE, user.getUserType());
                            }
                            startActivity(i);
                            finish();
                            //SplashScreenActivity.this.overridePendingTransition(0, 0);
                        }
                    });
                } else {
                    Intent i = new Intent(SplashscreenActivity.this, UserLoginActivity.class);
                    startActivity(i);
                    SplashscreenActivity.this.overridePendingTransition(0, 0);
                    finish();
                }
            }
        }, 3000);
    }

    protected void applyThemeMode(int themeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

}