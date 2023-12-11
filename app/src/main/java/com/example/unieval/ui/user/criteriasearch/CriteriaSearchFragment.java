package com.example.unieval.ui.user.criteriasearch;

import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.unieval.R;
import com.example.unieval.databinding.FragmentCriteriaSearchBinding;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.ui.user.evaluationresult.EvaluationListActivity;
import com.example.unieval.util.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class CriteriaSearchFragment extends Fragment {

    FragmentCriteriaSearchBinding binding;
    FirebaseAuth mAuth;

    ArrayAdapter<String> allCourseAdapter, internshipAdapter, studentTeacherRatioAdapter, graduateEmploymentPercentAdapter, facilityScoreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_criteria_search, container, false);

        String[] allCourses = getResources().getStringArray(R.array.all_course);
        allCourseAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, allCourses);
        binding.criteriaSearchStudentCourseSelectAutocomplete.setAdapter(allCourseAdapter);

        String[] facilityScore = getResources().getStringArray(R.array.score);
        facilityScoreAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, facilityScore);
        binding.criteriaSearchStudentFacilitiesScoreEditText.setAdapter(facilityScoreAdapter);

        String[] studentTeacherRatio = getResources().getStringArray(R.array.ratio);
        studentTeacherRatioAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, studentTeacherRatio);
        binding.criteriaSearchStudentTeacherStudentRatioEditText.setAdapter(studentTeacherRatioAdapter);

        String[] graduateEmploymentPercent = getResources().getStringArray(R.array.percent);
        graduateEmploymentPercentAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, graduateEmploymentPercent);
        binding.criteriaSearchStudentGradEmploymentRateEditText.setAdapter(graduateEmploymentPercentAdapter);

        String[] internship = getResources().getStringArray(R.array.choice);
        internshipAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, internship);
        binding.criteriaSearchStudentInternshipOpportunitiesEditText.setAdapter(internshipAdapter);

        // Set a default course so it wont be empty
        binding.criteriaSearchStudentCourseSelectAutocomplete.setText(allCourseAdapter.getItem(0), false);

        binding.seeResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntries();
            }
        });


        return binding.getRoot();
    }

    public void validateEntries() {
        String courseTitle = binding.criteriaSearchStudentCourseSelectAutocomplete.getText().toString();
        String minFees = binding.criteriaSearchStudentMinFeesEditText.getText().toString();
        String maxFees = binding.criteriaSearchStudentMaxFeesEditText.getText().toString();
        String facilityScore = binding.criteriaSearchStudentFacilitiesScoreEditText.getText().toString();
        String teacherStudentRatio = binding.criteriaSearchStudentTeacherStudentRatioEditText.getText().toString();
        String gradEmploymentPercent = binding.criteriaSearchStudentGradEmploymentRateEditText.getText().toString();
        String internship = binding.criteriaSearchStudentInternshipOpportunitiesEditText.getText().toString();

        if (!TextUtils.isEmpty(courseTitle) && !TextUtils.isEmpty(minFees) && !TextUtils.isEmpty(maxFees)
                && !TextUtils.isEmpty(facilityScore) && !TextUtils.isEmpty(teacherStudentRatio)
                && !TextUtils.isEmpty(gradEmploymentPercent) && !TextUtils.isEmpty(internship)) {
            if (maxGreaterThanMin(Long.parseLong(minFees), Long.parseLong(maxFees))) {
                UserMainActivity activity = (UserMainActivity) getActivity();
                String userType = activity.userType;
                Intent i = new Intent(getActivity(), EvaluationListActivity.class);
                i.putExtra(Constants.KEY_USER_TYPE, userType);
                i.putExtra("CourseTitle", courseTitle);
                i.putExtra("MaxFees", Long.parseLong(maxFees));
                i.putExtra("FacilityScore", mapFacilityScore(facilityScore));
                i.putExtra("TeacherStudentRatio", mapTeacherStudentRatio(teacherStudentRatio));
                i.putExtra("GradEmploymentPercent", mapGradEmploymentPercent(gradEmploymentPercent));
                i.putExtra("Internship", mapInternship(internship));
                startActivity(i);
            } else {
                Log.d("Min max", "false");
                Toast.makeText(getContext(), "Min fee is greater than Max", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean maxGreaterThanMin(long min, long max) {
        boolean checksOut = false;
        int compare = Long.compare(min, max);
        if (compare == -1) {
            checksOut = true;
        }
        return checksOut;
    }

    public int mapFacilityScore(String facilityScore) {
        int mFacilityScore;
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
        return mFacilityScore;
    }

    public int mapTeacherStudentRatio(String studentTeacherRatio) {
        int mStudentTeacherRatio;
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
        return mStudentTeacherRatio;
    }

    public int mapGradEmploymentPercent(String graduateEmploymentPercent) {
        int mGraduateEmploymentPercent;
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
        return mGraduateEmploymentPercent;
    }

    public boolean mapInternship(String internship) {
        boolean mInternship;
        // Internship
        if (internship.equals(internshipAdapter.getItem(0))) {
            mInternship = true;
        } else {
            mInternship = false;
        }
        return mInternship;
    }


}