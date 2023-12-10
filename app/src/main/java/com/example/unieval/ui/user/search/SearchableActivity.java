package com.example.unieval.ui.user.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.University;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivitySearchableBinding;
import com.example.unieval.ui.UniversityAdapter;
import com.example.unieval.ui.admin.managereview.overview.ReviewOverviewActivity;
import com.example.unieval.ui.admin.manageuniversity.category.UniversityCategoryActivity;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.example.unieval.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    ActivitySearchableBinding binding;
    UniversityAdapter universityAdapter;
    String query;
    BaseRepository baseRepository;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searchable);

        // Get the intent, verify the action, and get the query.
        Intent intent = getIntent();
        initAdapter();
        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            baseRepository.queryUniversity(query).observe(this, new Observer<List<University>>() {
                @Override
                public void onChanged(List<University> universities) {
                    if (universities != null) {
                        universityAdapter.setList(universities);
                        String title = universities.size() + " Search results";
                        binding.searchHomeToolbar.setTitle(title);
                        if (universities.size() == 0) {
                            binding.searchEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            binding.searchEmptyState.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

        binding.searchHomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.searchHomeRecyclerview.setLayoutManager(llm);
        binding.searchHomeRecyclerview.setHasFixedSize(true);
        universityAdapter = new UniversityAdapter(new UniversityAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String universityId) {
                if (firebaseUser != null) {
                    baseRepository.getUser(firebaseUser.getUid()).observe(SearchableActivity.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if (user != null) {
                                if (user.getUserType() != null) {
                                    // regular User
                                    if (user.getUserType().equals(Constants.USER_TYPE_STUDENT)) {
                                        // student
                                        Intent i = new Intent(SearchableActivity.this, UniversityDetailActivity.class);
                                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                                        i.putExtra(Constants.KEY_USER_TYPE, user.getUserType());
                                        startActivity(i);
                                    } else {
                                        // professor
                                        Intent i = new Intent(SearchableActivity.this, UniversityDetailActivity.class);
                                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                                        i.putExtra(Constants.KEY_USER_TYPE, user.getUserType());
                                        startActivity(i);
                                    }
                                } else {
                                    // admin
                                    if (user.getSessionType().equals(Constants.SESSION_TYPE_DATA_MANAGER)) {
                                        // data manager
                                        Intent i = new Intent(SearchableActivity.this, UniversityCategoryActivity.class);
                                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                                        startActivity(i);
                                    } else {
                                        // feedback manager
                                        Intent i = new Intent(SearchableActivity.this, ReviewOverviewActivity.class);
                                        i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                                        startActivity(i);
                                    }

                                }
                            }
                        }
                    });
                }


            }
        });
        binding.searchHomeRecyclerview.setAdapter(universityAdapter);
    }


}