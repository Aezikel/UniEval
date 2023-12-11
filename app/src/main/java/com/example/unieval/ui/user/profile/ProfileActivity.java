package com.example.unieval.ui.user.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    private static final int RC_PHOTO_PICKER = 2;
    BaseRepository baseRepository;
    String currentPhoto;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        baseRepository = new BaseRepository();

        firebaseAuth = FirebaseAuth.getInstance();

        String[] title = getResources().getStringArray(R.array.title);
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(ProfileActivity.this, R.layout.list_item, title);
        binding.profilePageTitleEditText.setAdapter(titleAdapter);

        String[] discipline = getResources().getStringArray(R.array.discipline);
        ArrayAdapter<String> disciplineAdapter = new ArrayAdapter<>(ProfileActivity.this, R.layout.list_item, discipline);
        binding.profilePageDisciplineEditText.setAdapter(disciplineAdapter);

        binding.profilePageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.profilePageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.done) {
                    validateCredentials();
                }
                return false;
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            //setting username, email and profile picture for current user
            baseRepository.getUser(firebaseAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        currentPhoto = user.getProfilePhoto();
                        binding.profilePageTitleEditText.setText(user.getTitle(), false);
                        binding.profilePageUserTypeEditText.setText(user.getUserType());
                        binding.profilePageFirstNameEditText.setText(user.getFirstName());
                        binding.profilePageLastNameEditText.setText(user.getLastName());
                        binding.profilePageEmailEditText.setText(user.getEmail());
                        binding.profilePageDisciplineEditText.setText(user.getDiscipline(), false);

                        if (!String.valueOf(user.getPhone()).equals("0")) {
                            String number = "0" + user.getPhone();
                            binding.profilePagePhoneEditText.setText(number);
                        }
                        if (currentPhoto != null) {
                            Glide.with(getApplicationContext()).load(currentPhoto).placeholder(R.drawable.preview_profile).into(binding.profilePageImageView);
                        }
                    }
                }
            });
        }

        binding.profilePageImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (data != null && user != null) {
                Uri selectedImageUri = data.getData();

                //Sending the chosen picture to firebase storage and creating a database reference to it
                baseRepository.updateProfilePhoto(firebaseAuth.getCurrentUser().getUid(), selectedImageUri, currentPhoto);
            }
        }
    }


    public void validateCredentials() {

        //Validate inputs
        String title = binding.profilePageTitleEditText.getText().toString();
        String firstName = binding.profilePageFirstNameEditText.getText().toString().trim();
        String lastName = binding.profilePageLastNameEditText.getText().toString().trim();
        String phone = binding.profilePagePhoneEditText.getText().toString();
        String discipline = binding.profilePageDisciplineEditText.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(discipline)) {
            if (firebaseAuth.getCurrentUser() != null) {
                long mobile = Long.parseLong(phone);
                User user = new User(title, firstName, lastName, mobile, discipline);
                baseRepository.updateUser(firebaseAuth.getCurrentUser().getUid(), user);
                finish();
            }
        } else {
            Toast.makeText(ProfileActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }

    }

}