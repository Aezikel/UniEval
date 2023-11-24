package com.example.unieval.ui.user.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.unieval.R;
import com.example.unieval.databinding.ActivityProfileBinding;
import com.example.unieval.ui.registeruser.RegisterUserActivity;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

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

    }
}