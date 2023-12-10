package com.example.unieval.ui.admin.manageuniversity.profile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.pojo.Profile;
import com.example.unieval.databinding.ActivityUniversityProfileBinding;
import com.example.unieval.util.Constants;

public class UniversityProfileActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;
    ActivityUniversityProfileBinding binding;
    Uri imageUri;
    String photo;

    Profile profileResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_profile);

        profileResult = getIntent().getParcelableExtra(Constants.KEY_INTENT_PROFILE);

        if (profileResult != null) {
            loadEntries(profileResult);
        }

        binding.uniProfileToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniProfileToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.done) {
                    validateCredentials();
                }
                return false;
            }
        });

        binding.uniProfileFab.setOnClickListener(new View.OnClickListener() {
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

    public void loadEntries(Profile profile) {
        binding.uniProfileToolbar.setTitle("Edit Profile");
        imageUri = profile.getPhotoUri();
        photo = profile.getPhoto();
        if (imageUri != null) {
            Glide.with(getApplicationContext()).load(imageUri).into(binding.uniImageImageView);
        } else {
            Glide.with(getApplicationContext()).load(photo).into(binding.uniImageImageView);
        }
        binding.uniNameTextInputEditText.setText(profile.getName());
        binding.uniOverviewTextInputEditText.setText(profile.getOverview());
        binding.uniMissionTextInputEditText.setText(profile.getMission());
        binding.uniRankTextInputEditText.setText(String.valueOf(profile.getRank()));
        binding.uniRatingTextInputEditText.setText(String.valueOf(profile.getRating()));
        binding.uniLocationTextInputEditText.setText(profile.getLocation());
        binding.uniEmailTextInputEditText.setText(profile.getEmail());
        binding.uniPhoneTextInputEditText.setText(String.valueOf(profile.getPhone()));
    }

    public void validateCredentials() {
        String uniName = binding.uniNameTextInputEditText.getText().toString().trim();
        String uniOverview = binding.uniOverviewTextInputEditText.getText().toString().trim();
        String uniMission = binding.uniMissionTextInputEditText.getText().toString().trim();
        String uniRank = binding.uniRankTextInputEditText.getText().toString().trim();
        String uniRating = binding.uniRatingTextInputEditText.getText().toString().trim();
        String uniLocation = binding.uniLocationTextInputEditText.getText().toString().trim();
        String uniEmail = binding.uniEmailTextInputEditText.getText().toString().trim();
        String uniPhone = binding.uniPhoneTextInputEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(uniName) && !TextUtils.isEmpty(uniOverview) && !TextUtils.isEmpty(uniMission)
                && !TextUtils.isEmpty(uniRank) && !TextUtils.isEmpty(uniRating) && !TextUtils.isEmpty(uniLocation)
                && !TextUtils.isEmpty(uniEmail) && !TextUtils.isEmpty(uniPhone)) {
            if ((imageUri == null)) {
                if (photo == null) {
                    Toast.makeText(UniversityProfileActivity.this, "Please add an image", Toast.LENGTH_SHORT).show();
                } else {
                    Profile profile = new Profile(photo, uniName, uniOverview, uniMission, Integer.parseInt(uniRank), Float.parseFloat(uniRating), uniLocation, uniEmail, Long.parseLong(uniPhone));
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Constants.KEY_INTENT_PROFILE, profile);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            } else {
                // user selected a photo
                Profile profile = new Profile(imageUri, uniName, uniOverview, uniMission, Integer.parseInt(uniRank), Float.parseFloat(uniRating), uniLocation, uniEmail, Long.parseLong(uniPhone));
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.KEY_INTENT_PROFILE, profile);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } else {
            Toast.makeText(UniversityProfileActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                Glide.with(getApplicationContext()).load(imageUri).into(binding.uniImageImageView);

                ContentResolver contentResolver = getContentResolver();
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                contentResolver.takePersistableUriPermission(imageUri, flag);
            }
        }
    }

}