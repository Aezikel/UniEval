package com.example.unieval.ui.user.universitydetails.reviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Review;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.FragmentTabReviewBinding;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.List;

public class TabReviewFragment extends Fragment {
    FragmentTabReviewBinding binding;
    ReviewAdapter reviewAdapter;
    String universityId;
    BaseRepository baseRepository;
    public TabReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_review, container, false);
        baseRepository = new BaseRepository();
        UniversityDetailActivity activity = (UniversityDetailActivity) getActivity();
        universityId = activity.universityId;

        Log.d("Is null?", universityId);
        initAdapter();

        if (universityId != null) {
            baseRepository.getAllUniversityReview(universityId).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
                @Override
                public void onChanged(List<Review> reviews) {
                    if (reviews != null) {
                        reviewAdapter.setList(reviews);
                        binding.textView11.setText(String.valueOf(reviews.size()));
                        if (reviews.size() == 0) {
                            binding.reviewEmptyState.setVisibility(View.VISIBLE);
                        } else {binding.reviewEmptyState.setVisibility(View.GONE);}
                    }
                }
            });

            baseRepository.getUniversity(universityId).observe(getViewLifecycleOwner(), new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        binding.textView.setText(String.valueOf(university.getUniversityProfile().getRating()));
                        binding.ratingBar.setRating(university.getUniversityProfile().getRating());
                    }
                }
            });
        }
        return binding.getRoot();
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.reviewRecyclerView.setLayoutManager(llm);
        binding.reviewRecyclerView.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(new ReviewAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String reviewId) {
                // Do nothing
            }
        });
        binding.reviewRecyclerView.setAdapter(reviewAdapter);
    }
}