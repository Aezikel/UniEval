package com.example.unieval.ui.user.evaluationsummary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.EvaluationSummary;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityEvaluationSummaryBinding;
import com.example.unieval.ui.EvaluationSummaryAdapter;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.example.unieval.util.Constants;

import java.util.List;

public class EvaluationSummaryActivity extends AppCompatActivity {

    ActivityEvaluationSummaryBinding binding;
    String userType;
    List<Integer> selectedIndex;
    List<String> allCriteria;
    String courseTitle, universityId;
    int criteriaScore;
    BaseRepository baseRepository;
    List<EvaluationSummary> evaluationSummaries;
    EvaluationSummaryAdapter evaluationSummaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_summary);

        userType = getIntent().getStringExtra(Constants.KEY_USER_TYPE);
        selectedIndex = getIntent().getIntegerArrayListExtra("SelectedIndexList");
        allCriteria = getIntent().getStringArrayListExtra("AllCriteria");
        courseTitle = getIntent().getStringExtra("CourseTitle");
        universityId = getIntent().getStringExtra("UniversityId");
        criteriaScore = getIntent().getIntExtra("CriteriaScore", 0);
        evaluationSummaries = getIntent().getParcelableArrayListExtra("EvaluationSummary");

        baseRepository = new BaseRepository();
        initAdapter();

        binding.reportToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.view_uni_info) {
                    Intent i = new Intent(EvaluationSummaryActivity.this, UniversityDetailActivity.class);
                    i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                    i.putExtra(Constants.KEY_USER_TYPE, userType);
                    startActivity(i);
                }
                return false;
            }
        });

        evaluationSummaryAdapter.setList(evaluationSummaries);

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(this, new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        binding.reportUniName.setText(university.getUniversityProfile().getName());
                        binding.reportLoccation.setText(university.getUniversityProfile().getLocation());
                        binding.reportCourse.setText(courseTitle);

                        String passed = criteriaScore + "/" + allCriteria.size();
                        binding.reportCriteriaPassed.setText(passed);

                        int i = criteriaScore;
                        int j = allCriteria.size();

                        double result = (double) i / j * 100;
                        String percent = result + "%";
                        binding.reportCriteriaScore.setText(percent);

                    }
                }
            });
        }
        binding.reportToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setHasFixedSize(true);
        evaluationSummaryAdapter = new EvaluationSummaryAdapter();
        binding.recyclerView.setAdapter(evaluationSummaryAdapter);
    }


}