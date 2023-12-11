package com.example.unieval.ui.auth.forgotpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.unieval.R;
import com.example.unieval.databinding.ActivityForgotPasswordBinding;
import com.example.unieval.ui.auth.loginuser.UserLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        binding.forgotPasswordConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to user login
                validateCredentials();
            }
        });

        binding.forgotPasswordSigninTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, UserLoginActivity.class));
                finish();
            }
        });

    }

    private void validateCredentials() {
        String email = binding.forgotPasswordEmailEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            sendRecoveryMail(email);
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendRecoveryMail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Recovery Mail", "Email sent.");
                            showMailSentDialog(email);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Recovery Mail", "failure", task.getException());
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showMailSentDialog(String email) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Check your email");
        builder.setMessage("Follow the instructions sent to " + email + " to recover your password.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(ForgotPasswordActivity.this, UserLoginActivity.class));
                finish();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(ForgotPasswordActivity.this, UserLoginActivity.class));
                finish();
            }
        });
        builder.show();

    }

}