package com.example.unieval.ui.admin.home;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.University;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivityAdminMainBinding;
import com.example.unieval.ui.UniversityAdapter;
import com.example.unieval.ui.admin.managereview.overview.ReviewOverviewActivity;
import com.example.unieval.ui.admin.manageuniversity.category.UniversityCategoryActivity;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.search.SearchableActivity;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    UniversityAdapter universityAdapter;
    BaseRepository baseRepository;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String sessionType;
    SearchView searchView;
    int themeMode;
    SharedPreferences preferences;
    int checkedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_main);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        sessionType = getIntent().getStringExtra(Constants.KEY_SESSION_TYPE);
        baseRepository = new BaseRepository();
        initAdapter();

        if (sessionType != null) {
            if (sessionType.equals(Constants.SESSION_TYPE_FEEDBACK_MANAGER)) {
                binding.adminFab.setVisibility(View.GONE);
            }
        }

        baseRepository.getAllUniversity().observe(this, new Observer<List<University>>() {
            @Override
            public void onChanged(List<University> universities) {
                if (universities != null) {
                    universityAdapter.setList(universities);
                    if (universities.size() == 0) {
                        binding.adminEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        binding.adminEmptyState.setVisibility(View.GONE);
                    }
                }
            }
        });

        binding.adminHomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.adminDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent i = new Intent(AdminMainActivity.this, UserLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };


        binding.adminNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.admin_nav_header_theme) {
                    binding.adminDrawerLayout.closeDrawer(GravityCompat.START);
                    showThemeSelectionDialog();
                }
                if (item.getItemId() == R.id.admin_nav_header_about) {
                    binding.adminDrawerLayout.closeDrawer(GravityCompat.START);
                    showAboutDialog();
                }
                if (item.getItemId() == R.id.admin_nav_header_logout) {
                    binding.adminDrawerLayout.closeDrawer(GravityCompat.START);
                    showLogOutDialog();
                }
                return false;
            }
        });

        binding.adminFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, UniversityCategoryActivity.class));
            }
        });

        if (mAuth.getCurrentUser() != null) {
            baseRepository.getUser(mAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    View header = binding.adminNavigationView.getHeaderView(0);
                    TextView email = header.findViewById(R.id.admin_Email);
                    TextView sessionType = header.findViewById(R.id.admin_sessionType);
                    email.setText(user.getEmail());
                    sessionType.setText(user.getSessionType());
                }
            });
        }

        // search
        binding.adminHomeToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.search_menu_item) {

                    // Get the SearchView and set the searchable configuration.
                    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                    searchView = (SearchView) item.getActionView();

                    ComponentName componentName = new ComponentName(AdminMainActivity.this, SearchableActivity.class);
                    SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);

                    // Assumes current activity is the searchable activity.
                    searchView.setSearchableInfo(searchableInfo);
                }
                return false;
            }
        });

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.adminHomeRecyclerview.setLayoutManager(llm);
        binding.adminHomeRecyclerview.setHasFixedSize(true);
        universityAdapter = new UniversityAdapter(new UniversityAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String universityId) {
                // for now
                if (sessionType != null) {
                    Intent i;
                    if (sessionType.equals(Constants.SESSION_TYPE_DATA_MANAGER)) {
                        // pass id for university
                        i = new Intent(AdminMainActivity.this, UniversityCategoryActivity.class);
                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                    } else {
                        i = new Intent(AdminMainActivity.this, ReviewOverviewActivity.class);
                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                    }
                    startActivity(i);
                }

            }
        });
        binding.adminHomeRecyclerview.setAdapter(universityAdapter);
    }

    public void showThemeSelectionDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AdminMainActivity.this)
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

                        ListView lw = ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView();
                        int checkedItem = lw.getCheckedItemPosition();

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

    @Override
    public void onBackPressed() {

        if (binding.adminDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.adminDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (searchView != null && !searchView.isIconified()) {
            binding.adminHomeToolbar.collapseActionView();
        }
        else {
            super.onBackPressed();
        }
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

}