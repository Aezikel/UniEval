package com.example.unieval.ui.auth.loginadmin;

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
import com.example.unieval.databinding.ActivityLoginAdminBinding;
import com.example.unieval.ui.admin.home.AdminMainActivity;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.example.unieval.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {
    ActivityLoginAdminBinding binding;
    BaseRepository baseRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_admin);

        baseRepository = new BaseRepository();
        mAuth = FirebaseAuth.getInstance();

        binding.loginAdminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to admin home
                validateCredentials();
            }
        });

        binding.loginAdminUserLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to user login
                startActivity(new Intent(AdminLoginActivity.this, UserLoginActivity.class));
                finish();
            }
        });
    }

    public void validateCredentials() {
        String email = binding.loginAdminEmailEditText.getText().toString().trim();
        String password = binding.loginAdminPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            signInAdmin(email, password);

        } else {
            Toast.makeText(AdminLoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInAdminEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User has logged in
                            if (mAuth.getCurrentUser() != null) {
                                baseRepository.getUser(mAuth.getCurrentUser().getUid()).observe(AdminLoginActivity.this, new Observer<User>() {
                                    @Override
                                    public void onChanged(User user) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent i = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                                        i.putExtra(Constants.KEY_SESSION_TYPE, user.getSessionType());
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressIndicator(false);
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    public void signInAdmin(String email, String password) {
        showProgressIndicator(true);
        baseRepository.isUserValid(email, Constants.ROLE_ADMIN).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    signInAdminEmailAndPassword(email, password);

                } else {
                    showProgressIndicator(false);
                    Toast.makeText(AdminLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
            binding.loginAdminLoginButton.setEnabled(false);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
            binding.loginAdminLoginButton.setEnabled(true);
        }
    }

}