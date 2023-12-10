package com.example.unieval.ui.auth.loginuser;

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
import com.example.unieval.ui.auth.forgotpassword.ForgotPasswordActivity;
import com.example.unieval.ui.auth.loginadmin.AdminLoginActivity;
import com.example.unieval.ui.auth.registeruser.RegisterUserActivity;
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

    }
}