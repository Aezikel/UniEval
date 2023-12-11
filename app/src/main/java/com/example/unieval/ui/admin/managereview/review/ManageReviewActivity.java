package com.example.unieval.ui.admin.managereview.review;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.unieval.data.pojo.Review;
import com.example.unieval.databinding.ActivityManageReviewBinding;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;

public class ManageReviewActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;
    ActivityManageReviewBinding binding;
    Uri imageUri;
    ArrayAdapter<String> reviewCategoryAdapter, reviewRatingAdapter;
    BaseRepository baseRepository;
    String universityId;
    String reviewId;
    String existingPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_review);

        universityId = getIntent().getStringExtra(Constants.KEY_INTENT_UNIVERSITY_ID);
        reviewId = getIntent().getStringExtra(Constants.KEY_INTENT_REVIEW_ID);

        baseRepository = new BaseRepository();

        String[] category = getResources().getStringArray(R.array.review_category);
        reviewCategoryAdapter = new ArrayAdapter<>(ManageReviewActivity.this, R.layout.list_item, category);
        binding.reviewerCategoryEditText.setAdapter(reviewCategoryAdapter);

        String[] rating = getResources().getStringArray(R.array.score);
        reviewRatingAdapter = new ArrayAdapter<>(ManageReviewActivity.this, R.layout.list_item, rating);
        binding.reviewerRatingEditText.setAdapter(reviewRatingAdapter);

        if (reviewId != null) {
            baseRepository.getReview(reviewId).observe(this, new Observer<Review>() {
                @Override
                public void onChanged(Review review) {

                    if (review != null) {
                        binding.reviewManageToolbar.setTitle("Edit Review");
                        binding.reviewManageToolbar.inflateMenu(R.menu.menu_review);
                        existingPhoto = review.getReviewerPhoto();
                        Glide.with(getApplicationContext()).load(existingPhoto).into(binding.reviewerImageView);
                        binding.reviewerNameEditText.setText(review.getReviewerName());
                        binding.reviewerCategoryEditText.setText(review.getReviewerClass(), false);
                        binding.reviewerRatingEditText.setText(unmapEntry(review.getReviewerRating()), false);
                        binding.reviewerMessageEditText.setText(review.getReviewMessage());
                    }
                }
            });
        } else {
            binding.reviewManageToolbar.inflateMenu(R.menu.menu_done);
        }

        binding.reviewManageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.reviewManageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.done) {
                    validateCredentials();
                }

                if (id == R.id.delete) {
                    showDeleteDialog();
                }
                return false;
            }
        });

        binding.reviewManageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

    }

    public void showDeleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete Review");
        builder.setMessage("Do you want to delete this review?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete
                baseRepository.saveReview(reviewId, null);
                baseRepository.deleteExistingPhoto(existingPhoto);
                Toast.makeText(ManageReviewActivity.this, "University deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void validateCredentials() {
        String reviewName = binding.reviewerNameEditText.getText().toString().trim();
        String reviewCategory = binding.reviewerCategoryEditText.getText().toString();
        String reviewRating = binding.reviewerRatingEditText.getText().toString();
        String reviewMessage = binding.reviewerMessageEditText.getText().toString().trim();
        int rating = mapEntry(reviewRating);

        if (!TextUtils.isEmpty(reviewName) && !TextUtils.isEmpty(reviewCategory) && !TextUtils.isEmpty(reviewRating)
                && !TextUtils.isEmpty(reviewMessage)) {
            if (existingPhoto == null) {
                if (imageUri == null) {
                    Toast.makeText(ManageReviewActivity.this, "Please add an image", Toast.LENGTH_SHORT).show();
                } else {
                    // save
                    if (universityId != null) {
                        showProgressIndicator(true);
                        String reviewId = baseRepository.getReviewId();
                        baseRepository.saveReviewPhoto(reviewId, imageUri).observe(this, new Observer<String>() {
                            @Override
                            public void onChanged(String downloadUrl) {
                                Review review = new Review(reviewId, universityId, downloadUrl, reviewName, reviewCategory, reviewMessage, rating);
                                baseRepository.saveReview(reviewId, review);
                                Toast.makeText(ManageReviewActivity.this, "Review added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
            } else {
                // update
                if (imageUri != null) {
                    showProgressIndicator(true);
                    // delete existing photo then reupload
                    baseRepository.deleteExistingPhoto(existingPhoto);
                    baseRepository.saveReviewPhoto(reviewId, imageUri).observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(String downloadUrl) {
                            Review review = new Review(reviewId, universityId, downloadUrl, reviewName, reviewCategory, reviewMessage, rating);
                            baseRepository.saveReview(reviewId, review);
                            Toast.makeText(ManageReviewActivity.this, "University updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    showProgressIndicator(true);
                    Review review = new Review(reviewId, universityId, existingPhoto, reviewName, reviewCategory, reviewMessage, rating);
                    baseRepository.saveReview(reviewId, review);
                    Toast.makeText(ManageReviewActivity.this, "University updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else {
            Toast.makeText(ManageReviewActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                Glide.with(getApplicationContext()).load(imageUri).into(binding.reviewerImageView);
            }
        }
    }

    public int mapEntry(String rating) {
        int mRating;
        // Rating
        if (rating.equals(reviewRatingAdapter.getItem(0))) {
            mRating = 1;
        } else if (rating.equals(reviewRatingAdapter.getItem(1))) {
            mRating = 2;
        } else if (rating.equals(reviewRatingAdapter.getItem(2))) {
            mRating = 3;
        } else if (rating.equals(reviewRatingAdapter.getItem(3))) {
            mRating = 4;
        } else {
            mRating = 5;
        }
        return mRating;
    }

    public String unmapEntry(int rating) {
        String mRating;
        // Rating
        if (rating == 1) {
            mRating = reviewRatingAdapter.getItem(0);
        } else if (rating == 2) {
            mRating = reviewRatingAdapter.getItem(1);
        } else if (rating == 3) {
            mRating = reviewRatingAdapter.getItem(2);
        } else if (rating == 4) {
            mRating = reviewRatingAdapter.getItem(3);
        } else {
            mRating = reviewRatingAdapter.getItem(4);
        }
        return mRating;
    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
        }
    }
}