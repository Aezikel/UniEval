package com.example.unieval.ui.loginadmin;

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
import com.example.unieval.databinding.ActivityLoginAdminBinding;
import com.example.unieval.ui.loginuser.UserLoginActivity;
import com.example.unieval.ui.user.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    ActivityLoginAdminBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_admin);

        mAuth = FirebaseAuth.getInstance();

        String[] sessionType = getResources().getStringArray(R.array.session_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminLoginActivity.this, R.layout.list_item, sessionType);
        binding.loginAdminSessionTypeAutoComplete.setAdapter(adapter);

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
        String sessionType = binding.loginAdminSessionTypeAutoComplete.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password)) {
            signInAdminEmailAndPassword(email, password, sessionType);
        } else {
            Toast.makeText(AdminLoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInAdminEmailAndPassword(String email, String password, String sessionType) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(AdminLoginActivity.this, UserMainActivity.class);
                            i.putExtra("SessionType", sessionType);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}