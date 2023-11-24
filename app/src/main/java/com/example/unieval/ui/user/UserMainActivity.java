package com.example.unieval.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unieval.R;
import com.example.unieval.databinding.ActivityUserMainBinding;
import com.example.unieval.ui.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.profile.ProfileActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMainActivity extends AppCompatActivity {

    ActivityUserMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController controller = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.homeFragment, R.id.criteriaSearchFragment, R.id.favoritesFragment)
                .setDrawerLayout(binding.userHomepageDrawerLayout).build();

        //Toolbar
        NavigationUI.setupWithNavController(binding.userHomepageToolbar, controller, appBarConfiguration);
        //Bottom NavBar
        NavigationUI.setupWithNavController(binding.userHomepageBottomNavView, controller);
        //Nav View
        NavigationUI.setupWithNavController(binding.userHomepageNavigationView, controller);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent i = new Intent(UserMainActivity.this, UserLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };


        binding.userHomepageNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_header_profile) {
                    startActivity(new Intent(UserMainActivity.this, ProfileActivity.class));
                    binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.nav_header_logout) {
                    binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
                    showLogOutDialog();
                }
                return false;
            }
        });

        if (mAuth.getCurrentUser() != null) {
            View header = binding.userHomepageNavigationView.getHeaderView(0);
            MaterialTextView name = header.findViewById(R.id.nav_header_userName_text);
            name.setText(mAuth.getCurrentUser().getDisplayName());
            ImageView image = header.findViewById(R.id.nav_header_imageView);
            image.setImageURI(mAuth.getCurrentUser().getPhotoUrl());
        }

    }

    public void showLogOutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Log out?");
        builder.setMessage("You are about to sign out");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        if (binding.userHomepageDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}