package com.example.unieval.ui.user.criteriasearchprofessor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.unieval.R;
import com.example.unieval.databinding.FragmentProfessorCriteriaSearchBinding;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.ui.user.evaluationresultprofessor.ProfessorEvaluationListActivity;
import com.example.unieval.util.Constants;

public class ProfessorCriteriaSearchFragment extends Fragment {

    FragmentProfessorCriteriaSearchBinding binding;
    ArrayAdapter<String> researchAdapter, workLifeBalanceAdapter, professionalDevelopmentAdapter, facilityScoreAdapter;

    public ProfessorCriteriaSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_professor_criteria_search, container, false);

        String[] allResearch = getResources().getStringArray(R.array.all_research);
        researchAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, allResearch);
        binding.criteriaSearchProfessorResearchSelectEditText.setAdapter(researchAdapter);

        String[] workLifeBalance = getResources().getStringArray(R.array.score);
        workLifeBalanceAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, workLifeBalance);
        binding.criteriaSearchProfessorWorkLifeEdittext.setAdapter(workLifeBalanceAdapter);

        String[] professionalDevelopment = getResources().getStringArray(R.array.choice);
        professionalDevelopmentAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, professionalDevelopment);
        binding.criteriaSearchProfessorProfessionalDevelopmentAutocomplete.setAdapter(professionalDevelopmentAdapter);

        String[] facilityScore = getResources().getStringArray(R.array.score);
        facilityScoreAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, facilityScore);
        binding.criteriaSearchProfessorLabScoreAutocomplete.setAdapter(facilityScoreAdapter);

        // Set a default course so it wont be empty
        binding.criteriaSearchProfessorResearchSelectEditText.setText(researchAdapter.getItem(0), false);

        binding.seeResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntries();
            }
        });


        return binding.getRoot();
    }

    public void validateEntries() {
        String researchTitle = binding.criteriaSearchProfessorResearchSelectEditText.getText().toString();
        String minSalary = binding.criteriaSearchProfessorMinExpectedSalaryEditText.getText().toString();
        String maxSalary = binding.criteriaSearchProfessorMaxExpectedSalaryEditText.getText().toString();
        String facilityScore = binding.criteriaSearchProfessorLabScoreAutocomplete.getText().toString();
        String workLife = binding.criteriaSearchProfessorWorkLifeEdittext.getText().toString();
        String professionalDev = binding.criteriaSearchProfessorProfessionalDevelopmentAutocomplete.getText().toString();

        if (!TextUtils.isEmpty(researchTitle) && !TextUtils.isEmpty(minSalary) && !TextUtils.isEmpty(maxSalary)
                && !TextUtils.isEmpty(facilityScore) && !TextUtils.isEmpty(workLife)
                && !TextUtils.isEmpty(professionalDev)) {
            if (maxGreaterThanMin(Long.parseLong(minSalary), Long.parseLong(maxSalary))) {
                UserMainActivity activity = (UserMainActivity) getActivity();
                String userType = activity.userType;
                Intent i = new Intent(getActivity(), ProfessorEvaluationListActivity.class);
                i.putExtra(Constants.KEY_USER_TYPE, userType);
                i.putExtra("ResearchTitle", researchTitle);
                i.putExtra("MaxSalary", Long.parseLong(maxSalary));
                i.putExtra("FacilityScore", mapFacilityScore(facilityScore));
                i.putExtra("WorkLife", mapWorkLife(workLife));
                i.putExtra("ProfessionalDev", mapProfessionalDev(professionalDev));
                startActivity(i);
            } else {
                Log.d("Min max", "false");
                Toast.makeText(getContext(), "Min Salary is greater than Max", Toast.LENGTH_SHORT).show();
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

    public int mapWorkLife(String workLifeBalance) {
        int mWorkLifeBalance;
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
        return mWorkLifeBalance;
    }

    public boolean mapProfessionalDev(String professionalDevelopment) {
        boolean mProfessionalDevelopment;
        // ProfessionalDevelopment
        if (professionalDevelopment.equals(professionalDevelopmentAdapter.getItem(0))) {
            mProfessionalDevelopment = true;
        } else {
            mProfessionalDevelopment = false;
        }
        return mProfessionalDevelopment;
    }


}