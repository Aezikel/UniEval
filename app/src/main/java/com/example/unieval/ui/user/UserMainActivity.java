package com.example.unieval.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivityUserMainBinding;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.profile.ProfileActivity;
import com.example.unieval.ui.user.search.SearchableActivity;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMainActivity extends AppCompatActivity {

    ActivityUserMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    BaseRepository baseRepository;

    public String userType;
    SearchView searchView;

    int themeMode;
    SharedPreferences preferences;
    int checkedItemPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        userType = getIntent().getStringExtra(Constants.KEY_USER_TYPE);
        Log.d("Intent received", userType);


        baseRepository = new BaseRepository();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController controller = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration;

        if (userType.equals(Constants.USER_TYPE_STUDENT)) {
            binding.userHomepageBottomNavView.inflateMenu(R.menu.user_homepage_bottom_nav_menu);
            appBarConfiguration = new AppBarConfiguration
                    .Builder(R.id.homeFragment, R.id.criteriaSearchFragment, R.id.favoritesFragment)
                    .setDrawerLayout(binding.userHomepageDrawerLayout).build();
        } else {
            binding.userHomepageBottomNavView.inflateMenu(R.menu.professor_homepage_bottom_nav_menu);
            appBarConfiguration = new AppBarConfiguration
                    .Builder(R.id.homeFragment, R.id.professorCriteriaSearchFragment, R.id.favoritesFragment)
                    .setDrawerLayout(binding.userHomepageDrawerLayout).build();
        }

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
                if (item.getItemId() == R.id.nav_header_theme) {
                    binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
                    showThemeSelectionDialog();
                }
                if (item.getItemId() == R.id.nav_header_about) {
                    binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
                    showAboutDialog();
                }
                if (item.getItemId() == R.id.nav_header_logout) {
                    binding.userHomepageDrawerLayout.closeDrawer(GravityCompat.START);
                    showLogOutDialog();
                }
                return false;
            }
        });

        if (mAuth.getCurrentUser() != null) {
            baseRepository.getUser(mAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        View header = binding.userHomepageNavigationView.getHeaderView(0);
                        TextView name = header.findViewById(R.id.nav_header_userName_text);
                        ImageView image = header.findViewById(R.id.nav_header_imageView);
                        name.setText(user.getFirstName());
                        Glide.with(getApplicationContext()).load(user.getProfilePhoto()).placeholder(R.drawable.preview_profile).into(image);
                    }
                }
            });
        }

        controller.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {
                binding.userHomepageToolbar.getMenu().findItem(R.id.search_menu_item).setVisible(true);
                binding.userHomepageToolbar.getMenu().findItem(R.id.result_menu_item).setVisible(false);
            }
            if (navDestination.getId() == R.id.criteriaSearchFragment) {
                binding.userHomepageToolbar.getMenu().findItem(R.id.search_menu_item).setVisible(false);
                binding.userHomepageToolbar.getMenu().findItem(R.id.result_menu_item).setVisible(false);
            }
            if (navDestination.getId() == R.id.favoritesFragment) {
                binding.userHomepageToolbar.getMenu().findItem(R.id.search_menu_item).setVisible(false);
                binding.userHomepageToolbar.getMenu().findItem(R.id.result_menu_item).setVisible(false);
            }
        });

        binding.userHomepageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.search_menu_item) {
                    // Get the SearchView and set the searchable configuration.
                    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                    searchView = (SearchView) item.getActionView();
                    ComponentName componentName = new ComponentName(UserMainActivity.this, SearchableActivity.class);
                    SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
                    // Assumes current activity is the searchable activity.
                    searchView.setSearchableInfo(searchableInfo);
                }
//                if (id == R.id.result_menu_item) {
//                    startActivity(new Intent(UserMainActivity.this, EvaluationListActivity.class));
//                }
                return false;
            }
        });


    }


    public void showThemeSelectionDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(UserMainActivity.this)
                .setTitle("Choose Theme")
                .setSingleChoiceItems(R.array.theme_mode, getCheckedItemPosition(), null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView listView = ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView();
                        int checkedItem = listView.getCheckedItemPosition();

                        switch (checkedItem) {
                            case 0:
                                themeMode = AppCompatDelegate.MODE_NIGHT_YES;
                                break;
                            case 1:
                                themeMode = AppCompatDelegate.MODE_NIGHT_NO;
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("ThemeMode", themeMode);
                        editor.apply();
                        applyThemeMode(themeMode);
                    }
                });
        materialAlertDialogBuilder.create().show();
    }



    public void showAboutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("About the app");
        builder.setMessage(R.string.lorem_ipsum);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
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
        } else if (searchView != null && !searchView.isIconified()) {
            binding.userHomepageToolbar.collapseActionView();
        } else {
            super.onBackPressed();
        }
    }

    protected void applyThemeMode(int themeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

    protected int getCheckedItemPosition() {
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            checkedItemPosition = 0;
        } else if (themeMode == AppCompatDelegate.MODE_NIGHT_NO) {
            checkedItemPosition = 1;
        }
        return checkedItemPosition;
    }

}