package com.example.unieval.ui.registeruser;

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
import com.example.unieval.databinding.ActivityRegisterUserBinding;
import com.example.unieval.ui.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterUserActivity extends AppCompatActivity {

    ActivityRegisterUserBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_user);

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
                createUser(email, password, firstName);
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


    public void createUser(String email, String password, String userName) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName).build();
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))

                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(RegisterUserActivity.this, UserMainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Failed Sign Up", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterUserActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}