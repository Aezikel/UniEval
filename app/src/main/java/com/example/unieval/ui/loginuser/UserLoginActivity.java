package com.example.unieval.ui.loginuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.unieval.R;
import com.example.unieval.databinding.ActivityLoginUserBinding;
import com.example.unieval.ui.forgotpassword.ForgotPasswordActivity;
import com.example.unieval.ui.loginadmin.AdminLoginActivity;
import com.example.unieval.ui.registeruser.RegisterUserActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLoginActivity extends AppCompatActivity {

    ActivityLoginUserBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_user);

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
            signInEmailAndPassword(email, password);
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
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(UserLoginActivity.this, UserMainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(UserLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}