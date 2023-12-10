package com.example.unieval.ui.auth.registeruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivityRegisterUserBinding;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUserActivity extends AppCompatActivity {

    ActivityRegisterUserBinding binding;
    private FirebaseAuth mAuth;
    BaseRepository baseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_user);

        baseRepository = new BaseRepository();

        mAuth = FirebaseAuth.getInstance();

        String[] userType = getResources().getStringArray(R.array.user_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterUserActivity.this, R.layout.list_item, userType);
        binding.registerUserTypeEditText.setAdapter(adapter);

        binding.registerPageDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to user homepage
                validateCredentials();
            }
        });

        binding.registerLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to user login
                startActivity(new Intent(RegisterUserActivity.this, UserLoginActivity.class));
                finish();
            }
        });
    }

    public void validateCredentials() {

        //Validate inputs
        String firstName = binding.registerFirstNameEditText.getText().toString().trim();
        String lastName = binding.registerLastNameEditText.getText().toString().trim();
        String userType = binding.registerUserTypeEditText.getText().toString();
        String email = binding.registerEmailEditText.getText().toString().trim();
        String password = binding.registerPasswordEditText.getText().toString();
        String confirmPassword = binding.registerConfirmPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(userType)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            if (password.equals(confirmPassword)) {
                String userCategory;
                if (userType.equals("Student - Looking for a school?")) {
                    userCategory = Constants.USER_TYPE_STUDENT;
                } else {
                    userCategory = Constants.USER_TYPE_PROFESSOR;
                }
                User user = new User(Constants.ROLE_USER, email, firstName, lastName, userCategory);
                createUser(user, password);
            } else {
                binding.registerPasswordTextInput.setErrorEnabled(true);
                binding.registerPasswordTextInput.setError("Password does not match");
                binding.registerConfirmPasswordTextInput.setErrorEnabled(true);
                binding.registerConfirmPasswordTextInput.setError("Password does not match");
            }
        } else {
            Toast.makeText(RegisterUserActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }


    public void createUser(User users, String password) {
        showProgressIndicator(true);
        mAuth.createUserWithEmailAndPassword(users.getEmail(), password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Save to Database
                        baseRepository.saveUser(user.getUid(), users);
                        Intent i = new Intent(RegisterUserActivity.this, UserMainActivity.class);
                        i.putExtra(Constants.KEY_USER_TYPE, users.getUserType());
                        startActivity(i);
                        finish();
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    showProgressIndicator(false);
                    Log.w("Failed Sign Up", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterUserActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
            binding.registerPageDoneBtn.setEnabled(false);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
            binding.registerPageDoneBtn.setEnabled(true);
        }
    }

}