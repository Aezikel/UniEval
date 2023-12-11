package com.example.unieval.ui.admin.manageuniversity.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Course;
import com.example.unieval.data.pojo.CriteriaScore;
import com.example.unieval.data.pojo.Profile;
import com.example.unieval.data.pojo.Research;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityUniversityCategoryBinding;
import com.example.unieval.ui.admin.manageuniversity.course.UniversityCourseActivity;
import com.example.unieval.ui.admin.manageuniversity.criteriascore.UniversityCriteriaScoreActivity;
import com.example.unieval.ui.admin.manageuniversity.profile.UniversityProfileActivity;
import com.example.unieval.ui.admin.manageuniversity.research.UniversityResearchActivity;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UniversityCategoryActivity extends AppCompatActivity {

    private static final int RC_PROFILE = 0;
    private static final int RC_CRITERIA_SCORE = 1;
    private static final int RC_COURSE = 2;
    private static final int RC_RESEARCH = 3;

    ActivityUniversityCategoryBinding binding;
    Profile profileResult;
    CriteriaScore criteriaScoreResult;
    List<Course> courseResult;
    List<Research> researchResult;
    University university;
    BaseRepository baseRepository;
    String universityId;
    String existingPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_category);

        baseRepository = new BaseRepository();

        universityId = getIntent().getStringExtra(Constants.KEY_INTENT_UNIVERSITY_ID);

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(this, new Observer<University>() {
                @Override
                public void onChanged(University uni) {
                    if (uni != null) {
                        binding.uniCategoryToolbar.setTitle(uni.getUniversityProfile().getName());
                        binding.textView2.setText("Edit Details of University in these Category");
                        existingPhotoUrl = uni.getUniversityProfile().getPhoto();

                        university = uni;
                        profileResult = uni.getUniversityProfile();
                        criteriaScoreResult = uni.getUniversityCriteriaScore();
                        courseResult = convertCourseHashmapToList(uni.getUniversityCourse());
                        researchResult = convertResearchHashmapToList(uni.getUniversityResearch());
                        binding.profileCheckImageView.setVisibility(View.VISIBLE);
                        binding.criteriaCheckImageView.setVisibility(View.VISIBLE);
                        binding.courseCheckImageView.setVisibility(View.VISIBLE);
                        binding.researchCheckImageView.setVisibility(View.VISIBLE);
                        binding.saveButton.setText("Update");
                        binding.uniCategoryToolbar.inflateMenu(R.menu.menu_delete);
                    }
                }
            });

        }

        baseRepository = new BaseRepository();

        binding.uniCategoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniCategoryToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.delete) {
                    showDeleteDialog();
                }
                return false;
            }
        });

        binding.profileMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UniversityCategoryActivity.this, UniversityProfileActivity.class);
                i.putExtra(Constants.KEY_INTENT_PROFILE, profileResult);
                startActivityForResult(i, RC_PROFILE);
            }
        });

        binding.criteriaMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UniversityCategoryActivity.this, UniversityCriteriaScoreActivity.class);
                i.putExtra(Constants.KEY_INTENT_CRITERIA_SCORE, criteriaScoreResult);
                startActivityForResult(i, RC_CRITERIA_SCORE);
            }
        });

        binding.courseMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UniversityCategoryActivity.this, UniversityCourseActivity.class);
                i.putParcelableArrayListExtra(Constants.KEY_INTENT_COURSE, (ArrayList<? extends Parcelable>) courseResult);
                startActivityForResult(i, RC_COURSE);
            }
        });

        binding.researchMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UniversityCategoryActivity.this, UniversityResearchActivity.class);
                i.putParcelableArrayListExtra(Constants.KEY_INTENT_RESEARCH, (ArrayList<? extends Parcelable>) researchResult);
                startActivityForResult(i, RC_RESEARCH);
            }
        });


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });
    }


    public void showDeleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete University");
        builder.setMessage("Do you want to delete this university?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete
                baseRepository.deleteExistingPhoto(existingPhotoUrl);
                baseRepository.saveUniversity(universityId, null);
                Toast.makeText(UniversityCategoryActivity.this, "University deleted successfully", Toast.LENGTH_SHORT).show();
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
        // validate and save to firebase
        if (profileResult != null && criteriaScoreResult != null && courseResult != null & researchResult != null) {
            if (universityId != null) {
                // update method
                if (profileResult.getPhotoUri() != null) {
                    showProgressIndicator(true);

                    // delete existing photo then reupload
                    // add a check for existing photo deletion later


                    baseRepository.deleteExistingPhoto(existingPhotoUrl);
                    baseRepository.saveUniversityPhoto(universityId, profileResult.getPhotoUri()).observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(String downloadUrl) {
                            Profile updatedProfile = new Profile(downloadUrl, profileResult.getName(), profileResult.getOverview(), profileResult.getMission(),
                                    profileResult.getRank(), profileResult.getRating(), profileResult.getLocation(), profileResult.getEmail(), profileResult.getPhone());
                            university = new University(universityId, updatedProfile, criteriaScoreResult, convertCourseListToHashmap(courseResult), convertResearchListToHashmap(researchResult));
                            // save to db
                            baseRepository.updateUniversity(universityId, university);
                            Toast.makeText(UniversityCategoryActivity.this, "University updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    showProgressIndicator(true);
                    Profile updatedProfile = new Profile(existingPhotoUrl, profileResult.getName(), profileResult.getOverview(), profileResult.getMission(),
                            profileResult.getRank(), profileResult.getRating(), profileResult.getLocation(), profileResult.getEmail(), profileResult.getPhone());
                    university = new University(universityId, updatedProfile, criteriaScoreResult, convertCourseListToHashmap(courseResult), convertResearchListToHashmap(researchResult));
                    // save to db
                    baseRepository.updateUniversity(universityId, university);
                    Toast.makeText(UniversityCategoryActivity.this, "University updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } else {
                showProgressIndicator(true);
                // save method
                String universityId = baseRepository.getUniversityId();
                baseRepository.saveUniversityPhoto(universityId, profileResult.getPhotoUri()).observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String downloadUrl) {
                        Profile updatedProfile = new Profile(downloadUrl, profileResult.getName(), profileResult.getOverview(), profileResult.getMission(),
                                profileResult.getRank(), profileResult.getRating(), profileResult.getLocation(), profileResult.getEmail(), profileResult.getPhone());
                        university = new University(universityId, updatedProfile, criteriaScoreResult, convertCourseListToHashmap(courseResult), convertResearchListToHashmap(researchResult));
                        // save to db
                        baseRepository.saveUniversity(universityId, university);
                        Toast.makeText(UniversityCategoryActivity.this, "University added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(UniversityCategoryActivity.this, "Please complete all category", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PROFILE && resultCode == RESULT_OK) {
            // receive input
            if (data != null) {
                profileResult = data.getParcelableExtra(Constants.KEY_INTENT_PROFILE);
                binding.profileCheckImageView.setVisibility(View.VISIBLE);

                Log.d("Category", profileResult.getName());
            }
        }
        // receive input
        if (requestCode == RC_CRITERIA_SCORE && resultCode == RESULT_OK) {
            // receive input
            if (data != null) {
                criteriaScoreResult = data.getParcelableExtra(Constants.KEY_INTENT_CRITERIA_SCORE);
                binding.criteriaCheckImageView.setVisibility(View.VISIBLE);
                Log.d("Category", String.valueOf(criteriaScoreResult.isInternship()));
            }
        }
        if (requestCode == RC_COURSE && resultCode == RESULT_OK) {

            // receive input
            if (data != null) {
                courseResult = data.getParcelableArrayListExtra(Constants.KEY_INTENT_COURSE);
                binding.courseCheckImageView.setVisibility(View.VISIBLE);
                Log.d("Category", courseResult.get(0).getCourseTitle());
            }
        }
        if (requestCode == RC_RESEARCH && resultCode == RESULT_OK) {

            // receive input
            if (data != null) {
                researchResult = data.getParcelableArrayListExtra(Constants.KEY_INTENT_RESEARCH);
                binding.researchCheckImageView.setVisibility(View.VISIBLE);
                Log.d("Category", researchResult.get(0).getResearchTitle());
            }
        }
    }

    public void showProgressIndicator(Boolean showIndicator) {

        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
            binding.saveButton.setEnabled(false);

        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
            binding.saveButton.setEnabled(true);
        }
    }

    public HashMap<String, Course> convertCourseListToHashmap(List<Course> courseList) {

        HashMap<String, Course> courseHashMap = new HashMap<>();
        for (Course c : courseList) {
            courseHashMap.put(c.getCourseTitle(), c);
        }
        return courseHashMap;
    }

    public List<Course> convertCourseHashmapToList(HashMap<String, Course> courseHashmap) {
        return new ArrayList<>(courseHashmap.values());
    }

    public HashMap<String, Research> convertResearchListToHashmap(List<Research> researchList) {
        HashMap<String, Research> researchHashMap = new HashMap<>();
        for (Research r : researchList) {
            researchHashMap.put(r.getResearchTitle(), r);
        }
        return researchHashMap;
    }

    public List<Research> convertResearchHashmapToList(HashMap<String, Research> researchHashmap) {
        return new ArrayList<>(researchHashmap.values());
    }


}