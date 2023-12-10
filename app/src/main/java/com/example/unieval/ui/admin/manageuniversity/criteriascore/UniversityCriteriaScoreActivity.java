package com.example.unieval.ui.admin.manageuniversity.criteriascore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.unieval.R;
import com.example.unieval.data.pojo.CriteriaScore;
import com.example.unieval.databinding.ActivityUniversityCriteriaScoreBinding;
import com.example.unieval.util.Constants;

public class UniversityCriteriaScoreActivity extends AppCompatActivity {

    ActivityUniversityCriteriaScoreBinding binding;
    ArrayAdapter<String> internshipAdapter, studentTeacherRatioAdapter, graduateEmploymentPercentAdapter, workLifeBalanceAdapter, professionalDevelopmentAdapter, facilityScoreAdapter;

    CriteriaScore criteriaScoreResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_criteria_score);

        criteriaScoreResult = getIntent().getParcelableExtra(Constants.KEY_INTENT_CRITERIA_SCORE);

        String[] internship = getResources().getStringArray(R.array.choice);
        internshipAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, internship);
        binding.uniCriteriaInternshipTextInputEditText.setAdapter(internshipAdapter);

        String[] studentTeacherRatio = getResources().getStringArray(R.array.ratio);
        studentTeacherRatioAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, studentTeacherRatio);
        binding.uniCriteriaStudentTeacherTextInputEditText.setAdapter(studentTeacherRatioAdapter);

        String[] graduateEmploymentPercent = getResources().getStringArray(R.array.percent);
        graduateEmploymentPercentAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, graduateEmploymentPercent);
        binding.uniCriteriaGraduateTextInputEditText.setAdapter(graduateEmploymentPercentAdapter);

        String[] workLifeBalance = getResources().getStringArray(R.array.score);
        workLifeBalanceAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, workLifeBalance);
        binding.uniCriteriaWorkLifeTextInputEditText.setAdapter(workLifeBalanceAdapter);

        String[] professionalDevelopment = getResources().getStringArray(R.array.choice);
        professionalDevelopmentAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, professionalDevelopment);
        binding.uniCriteriaProfessionalDevTextInputEditText.setAdapter(professionalDevelopmentAdapter);

        String[] facilityScore = getResources().getStringArray(R.array.score);
        facilityScoreAdapter = new ArrayAdapter<>(UniversityCriteriaScoreActivity.this, R.layout.list_item, facilityScore);
        binding.uniCriteriaFacilityScoreTextInputEditText.setAdapter(facilityScoreAdapter);

        if (criteriaScoreResult != null) {
            loadEntries(criteriaScoreResult);
        }

        binding.uniCriteriaScoreToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniCriteriaScoreToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.done) {
                    validateCredentials();
                }
                return false;
            }
        });

    }

    public void validateCredentials() {
        String internship = binding.uniCriteriaInternshipTextInputEditText.getText().toString();
        String studentTeacherRatio = binding.uniCriteriaStudentTeacherTextInputEditText.getText().toString();
        String graduateEmploymentPercent = binding.uniCriteriaGraduateTextInputEditText.getText().toString();
        String workLifeBalance = binding.uniCriteriaWorkLifeTextInputEditText.getText().toString();
        String professionalDevelopment = binding.uniCriteriaProfessionalDevTextInputEditText.getText().toString();
        String facilityScore = binding.uniCriteriaFacilityScoreTextInputEditText.getText().toString();

        if (!TextUtils.isEmpty(internship) && !TextUtils.isEmpty(studentTeacherRatio) && !TextUtils.isEmpty(graduateEmploymentPercent)
                && !TextUtils.isEmpty(workLifeBalance) && !TextUtils.isEmpty(professionalDevelopment) && !TextUtils.isEmpty(facilityScore)) {
            CriteriaScore criteriaScore = mapEntries(internship, studentTeacherRatio, graduateEmploymentPercent, workLifeBalance, professionalDevelopment, facilityScore);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.KEY_INTENT_CRITERIA_SCORE, criteriaScore);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(UniversityCriteriaScoreActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadEntries(CriteriaScore criteriaScore) {

        binding.uniCriteriaScoreToolbar.setTitle("Edit Criteria Score");

        // Internship
        if (criteriaScore.isInternship()) {
            binding.uniCriteriaInternshipTextInputEditText.setText(internshipAdapter.getItem(0), false);
        } else {
            binding.uniCriteriaInternshipTextInputEditText.setText(internshipAdapter.getItem(1), false);
        }

        // StudentTeacherRatio
        if (criteriaScore.getStudentTeacherRatio() == 1) {
            binding.uniCriteriaStudentTeacherTextInputEditText.setText(studentTeacherRatioAdapter.getItem(0), false);
        } else if (criteriaScore.getStudentTeacherRatio() == 2) {
            binding.uniCriteriaStudentTeacherTextInputEditText.setText(studentTeacherRatioAdapter.getItem(1), false);
        } else if (criteriaScore.getStudentTeacherRatio() == 3) {
            binding.uniCriteriaStudentTeacherTextInputEditText.setText(studentTeacherRatioAdapter.getItem(2), false);
        } else if (criteriaScore.getStudentTeacherRatio() == 4) {
            binding.uniCriteriaStudentTeacherTextInputEditText.setText(studentTeacherRatioAdapter.getItem(3), false);
        } else {
            binding.uniCriteriaStudentTeacherTextInputEditText.setText(studentTeacherRatioAdapter.getItem(4), false);
        }

        // GraduateEmploymentPercent
        if (criteriaScore.getGraduateEmploymentRatio() == 1) {
            binding.uniCriteriaGraduateTextInputEditText.setText(graduateEmploymentPercentAdapter.getItem(0), false);
        } else if (criteriaScore.getGraduateEmploymentRatio() == 2) {
            binding.uniCriteriaGraduateTextInputEditText.setText(graduateEmploymentPercentAdapter.getItem(1), false);
        } else if (criteriaScore.getGraduateEmploymentRatio() == 3) {
            binding.uniCriteriaGraduateTextInputEditText.setText(graduateEmploymentPercentAdapter.getItem(2), false);
        } else {
            binding.uniCriteriaGraduateTextInputEditText.setText(graduateEmploymentPercentAdapter.getItem(3), false);
        }

        // WorkLifeBalance
        if (criteriaScore.getWorkLifeBalance() == 1) {
            binding.uniCriteriaWorkLifeTextInputEditText.setText(workLifeBalanceAdapter.getItem(0), false);
        } else if (criteriaScore.getWorkLifeBalance() == 2) {
            binding.uniCriteriaWorkLifeTextInputEditText.setText(workLifeBalanceAdapter.getItem(1), false);
        } else if (criteriaScore.getWorkLifeBalance() == 3) {
            binding.uniCriteriaWorkLifeTextInputEditText.setText(workLifeBalanceAdapter.getItem(2), false);
        } else if (criteriaScore.getWorkLifeBalance() == 4) {
            binding.uniCriteriaWorkLifeTextInputEditText.setText(workLifeBalanceAdapter.getItem(3), false);
        } else {
            binding.uniCriteriaWorkLifeTextInputEditText.setText(workLifeBalanceAdapter.getItem(4), false);
        }

        // ProfessionalDevelopment
        if (criteriaScore.isProfessionalDevelopment()) {
            binding.uniCriteriaProfessionalDevTextInputEditText.setText(professionalDevelopmentAdapter.getItem(0), false);
        } else {
            binding.uniCriteriaProfessionalDevTextInputEditText.setText(professionalDevelopmentAdapter.getItem(1), false);
        }

        // FacilityScore
        if (criteriaScore.getFacilityScore() == 1) {
            binding.uniCriteriaFacilityScoreTextInputEditText.setText(facilityScoreAdapter.getItem(0), false);
        } else if (criteriaScore.getFacilityScore() == 2) {
            binding.uniCriteriaFacilityScoreTextInputEditText.setText(facilityScoreAdapter.getItem(1), false);
        } else if (criteriaScore.getFacilityScore() == 3) {
            binding.uniCriteriaFacilityScoreTextInputEditText.setText(facilityScoreAdapter.getItem(2), false);
        } else if (criteriaScore.getFacilityScore() == 4) {
            binding.uniCriteriaFacilityScoreTextInputEditText.setText(facilityScoreAdapter.getItem(3), false);
        } else {
            binding.uniCriteriaFacilityScoreTextInputEditText.setText(facilityScoreAdapter.getItem(4), false);
        }

    }

    public CriteriaScore mapEntries(String internship, String studentTeacherRatio, String graduateEmploymentPercent, String workLifeBalance, String professionalDevelopment, String facilityScore) {

        boolean mInternship, mProfessionalDevelopment;
        int mStudentTeacherRatio, mGraduateEmploymentPercent, mWorkLifeBalance, mFacilityScore;

        // Internship
        if (internship.equals(internshipAdapter.getItem(0))) {
            mInternship = true;
        } else {
            mInternship = false;
        }

        // StudentTeacherRatio
        if (studentTeacherRatio.equals(studentTeacherRatioAdapter.getItem(0))) {
            mStudentTeacherRatio = 1;
        } else if (studentTeacherRatio.equals(studentTeacherRatioAdapter.getItem(1))) {
            mStudentTeacherRatio = 2;
        } else if (studentTeacherRatio.equals(studentTeacherRatioAdapter.getItem(2))) {
            mStudentTeacherRatio = 3;
        } else if (studentTeacherRatio.equals(studentTeacherRatioAdapter.getItem(3))) {
            mStudentTeacherRatio = 4;
        } else {
            mStudentTeacherRatio = 5;
        }

        // GraduateEmploymentPercent
        if (graduateEmploymentPercent.equals(graduateEmploymentPercentAdapter.getItem(0))) {
            mGraduateEmploymentPercent = 1;
        } else if (graduateEmploymentPercent.equals(graduateEmploymentPercentAdapter.getItem(1))) {
            mGraduateEmploymentPercent = 2;
        } else if (graduateEmploymentPercent.equals(graduateEmploymentPercentAdapter.getItem(2))) {
            mGraduateEmploymentPercent = 3;
        } else {
            mGraduateEmploymentPercent = 4;
        }

        // WorkLifeBalance
        if (workLifeBalance.equals(workLifeBalanceAdapter.getItem(0))) {
            mWorkLifeBalance = 1;
        } else if (workLifeBalance.equals(workLifeBalanceAdapter.getItem(1))) {
            mWorkLifeBalance = 2;
        } else if (workLifeBalance.equals(workLifeBalanceAdapter.getItem(2))) {
            mWorkLifeBalance = 3;
        } else if (workLifeBalance.equals(workLifeBalanceAdapter.getItem(3))) {
            mWorkLifeBalance = 4;
        } else {
            mWorkLifeBalance = 5;
        }

        // ProfessionalDevelopment
        if (professionalDevelopment.equals(professionalDevelopmentAdapter.getItem(0))) {
            mProfessionalDevelopment = true;
        } else {
            mProfessionalDevelopment = false;
        }

        // FacilityScore
        if (facilityScore.equals(facilityScoreAdapter.getItem(0))) {
            mFacilityScore = 1;
        } else if (facilityScore.equals(facilityScoreAdapter.getItem(1))) {
            mFacilityScore = 2;
        } else if (facilityScore.equals(facilityScoreAdapter.getItem(2))) {
            mFacilityScore = 3;
        } else if (facilityScore.equals(facilityScoreAdapter.getItem(3))) {
            mFacilityScore = 4;
        } else {
            mFacilityScore = 5;
        }

        return new CriteriaScore(mInternship, mStudentTeacherRatio, mGraduateEmploymentPercent, mWorkLifeBalance, mProfessionalDevelopment, mFacilityScore);
    }


}