package com.example.unieval.ui.auth.loginuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.User;
import com.example.unieval.databinding.ActivityLoginUserBinding;
import com.example.unieval.ui.admin.home.AdminMainActivity;
import com.example.unieval.ui.auth.forgotpassword.ForgotPasswordActivity;
import com.example.unieval.ui.auth.loginadmin.AdminLoginActivity;
import com.example.unieval.ui.auth.registeruser.RegisterUserActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLoginActivity extends AppCompatActivity {

    ActivityLoginUserBinding binding;
    private FirebaseAuth mAuth;
    BaseRepository baseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_user);

        baseRepository = new BaseRepository();

        mAuth = FirebaseAuth.getInstance();

        binding.loginUserForgotPasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to forgot password screen
                startActivity(new Intent(UserLoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        binding.loginUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to user home page
                validateCredentials();
            }
        });

        binding.loginUserSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to register screen
                startActivity(new Intent(UserLoginActivity.this, RegisterUserActivity.class));
                finish();
            }
        });

        binding.loginUserAdministratorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to admin login screen
                startActivity(new Intent(UserLoginActivity.this, AdminLoginActivity.class));
                finish();
            }
        });


    }

    public void validateCredentials() {
        String email = binding.loginUserEmailEditText.getText().toString().trim();
        String password = binding.loginUserPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            signInUser(email, password);
        } else {
            Toast.makeText(UserLoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User has logged in
                            if (mAuth.getCurrentUser() != null) {
                                baseRepository.getUser(mAuth.getCurrentUser().getUid()).observe(UserLoginActivity.this, new Observer<User>() {
                                    @Override
                                    public void onChanged(User user) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent i = new Intent(UserLoginActivity.this, UserMainActivity.class);
                                        i.putExtra(Constants.KEY_USER_TYPE, user.getUserType());
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressIndicator(false);
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(UserLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void signInUser(String email, String password) {
        showProgressIndicator(true);
        baseRepository.isUserValid(email, Constants.ROLE_USER).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    signInEmailAndPassword(email, password);
                } else {
                    showProgressIndicator(false);
                    Toast.makeText(UserLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
            binding.loginUserLoginButton.setEnabled(false);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
            binding.loginUserLoginButton.setEnabled(true);
        }
    }

}