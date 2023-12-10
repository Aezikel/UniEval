package com.example.unieval.ui.user.universitydetails;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityUniversityDetailBinding;
import com.example.unieval.util.Constants;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;


public class UniversityDetailActivity extends AppCompatActivity {

    ActivityUniversityDetailBinding binding;
    public String universityId;
    UniversityDetailAdapter universityDetailAdapter;
    String userType;
    FirebaseAuth mAuth;
    BaseRepository baseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_detail);

        userType = getIntent().getStringExtra(Constants.KEY_USER_TYPE);
        universityId = getIntent().getStringExtra(Constants.KEY_INTENT_UNIVERSITY_ID);
        baseRepository = new BaseRepository();
        mAuth = FirebaseAuth.getInstance();

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(this, new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        Glide.with(getApplicationContext()).load(university.getUniversityProfile().getPhoto()).into(binding.uniImageView);
                        binding.textView12.setText(university.getUniversityProfile().getName());
                        binding.researchCountTextview.setText(String.valueOf(university.getUniversityResearch().size()));
                        binding.courseCountTextview.setText(String.valueOf(university.getUniversityCourse().size()));
                        binding.ratingCountTextview.setText(String.valueOf(university.getUniversityProfile().getRating()));
                    }
                }
            });

            if (mAuth.getCurrentUser() != null) {
                baseRepository.isFavourite(mAuth.getCurrentUser().getUid(), universityId).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isFavorite) {
                        if (isFavorite != null) {
                            if (isFavorite) {
                                binding.uniDetailsToolbar.getMenu().clear();
                                binding.uniDetailsToolbar.inflateMenu(R.menu.menu_favourite_selected);
                            } else {
                                binding.uniDetailsToolbar.getMenu().clear();
                                binding.uniDetailsToolbar.inflateMenu(R.menu.menu_favourite);
                            }
                        }
                    }
                });
            }
        }

        if (userType != null) {
            String[] headerTitle = new String[3];
            if (userType.equals(Constants.USER_TYPE_STUDENT)) {
                headerTitle[0] = "About";
                headerTitle[1] = "Courses";
                headerTitle[2] = "Review";
                universityDetailAdapter = new UniversityDetailAdapter(getSupportFragmentManager(), getLifecycle());
                universityDetailAdapter.setUserType(userType);
                binding.uniDetailsViewpager2.setAdapter(universityDetailAdapter);
            } else {
                headerTitle[0] = "About";
                headerTitle[1] = "Research";
                headerTitle[2] = "Review";
                universityDetailAdapter = new UniversityDetailAdapter(getSupportFragmentManager(), getLifecycle());
                universityDetailAdapter.setUserType(userType);
                binding.uniDetailsViewpager2.setAdapter(universityDetailAdapter);
            }
            new TabLayoutMediator(binding.uniDetailsTabLayout, binding.uniDetailsViewpager2, (tab, position) -> {
                tab.setText(headerTitle[position]);
            }).attach();
        }


        binding.uniDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniDetailsToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.favourite_empty) {
                    if (mAuth.getCurrentUser() != null) {
                        baseRepository.addToFavourite(universityId, mAuth.getCurrentUser().getUid());
                        Toast.makeText(UniversityDetailActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
                if (id == R.id.favourite_filled) {
                    if (mAuth.getCurrentUser() != null) {
                        baseRepository.removeFromFavourite(universityId, mAuth.getCurrentUser().getUid());
                        Toast.makeText(UniversityDetailActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

    }

}