package com.example.unieval.ui.admin.managereview.overview;

import android.content.Intent;
import android.os.Bundle;

import com.example.unieval.ui.user.universitydetails.reviews.ReviewAdapter;
import com.example.unieval.util.Constants;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Review;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityReviewOverviewBinding;
import com.example.unieval.ui.admin.managereview.review.ManageReviewActivity;

import java.util.List;

public class ReviewOverviewActivity extends AppCompatActivity {

    ActivityReviewOverviewBinding binding;
    ReviewAdapter reviewAdapter;
    String universityId;
    BaseRepository baseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_overview);

        universityId = getIntent().getStringExtra(Constants.KEY_INTENT_UNIVERSITY_ID);
        baseRepository = new BaseRepository();
        initAdapter();

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(this, new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        Glide.with(getApplicationContext()).load(university.getUniversityProfile().getPhoto()).into(binding.reviewImageView);
                        binding.reviewUniNameTextView.setText(university.getUniversityProfile().getName());
                        binding.researchCountTextview.setText(String.valueOf(university.getUniversityResearch().size()));
                        binding.courseCountTextview.setText(String.valueOf(university.getUniversityCourse().size()));
                        binding.ratingCountTextview.setText(String.valueOf(university.getUniversityProfile().getRating()));
                    }
                }
            });

            baseRepository.getAllUniversityReview(universityId).observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(List<Review> reviews) {
                    if (reviews != null) {
                        // consider adding an empty state
                        reviewAdapter.setList(reviews);
                        if (reviews.size() == 0) {
                            binding.reviewEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            binding.reviewEmptyState.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

        binding.reviewOverviewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.reviewOverviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewOverviewActivity.this, ManageReviewActivity.class);
                i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                startActivity(i);
            }
        });
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.reviewRecyclerView.setLayoutManager(llm);
        binding.reviewRecyclerView.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(new ReviewAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String reviewId) {
                Intent i = new Intent(ReviewOverviewActivity.this, ManageReviewActivity.class);
                i.putExtra(Constants.KEY_INTENT_REVIEW_ID, reviewId);
                i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                startActivity(i);
            }
        });
        binding.reviewRecyclerView.setAdapter(reviewAdapter);
    }

}