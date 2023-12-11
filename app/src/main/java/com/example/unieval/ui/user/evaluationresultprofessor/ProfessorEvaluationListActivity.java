package com.example.unieval.ui.user.evaluationresultprofessor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Criteria;
import com.example.unieval.data.pojo.EvaluationSummary;
import com.example.unieval.data.pojo.Research;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityProfessorEvaluationListBinding;
import com.example.unieval.ui.CriteriaAdapter;
import com.example.unieval.ui.user.evaluationsummaryprofessor.ProfessorEvaluationSummaryActivity;
import com.example.unieval.util.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ProfessorEvaluationListActivity extends AppCompatActivity {

    ActivityProfessorEvaluationListBinding binding;
    BaseRepository baseRepository;
    CriteriaAdapter criteriaAdapter;

    String userType;

    String researchTitle;
    long maxSalary;
    int facilityScore, workLifeBalance;
    boolean professionalDev;

    HashMap<String, List<Integer>> criteriaHashmap;
    List<String> allCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professor_evaluation_list);

        criteriaHashmap = new HashMap<>();
        allCriteria = new ArrayList<>();

        userType = getIntent().getStringExtra(Constants.KEY_USER_TYPE);
        researchTitle = getIntent().getStringExtra("ResearchTitle");
        maxSalary = getIntent().getLongExtra("MaxSalary", 0);
        workLifeBalance = getIntent().getIntExtra("WorkLife", 0);
        facilityScore = getIntent().getIntExtra("FacilityScore", 0);
        professionalDev = getIntent().getBooleanExtra("ProfessionalDev", false);

        baseRepository = new BaseRepository();
        initAdapter();

        allCriteria.add("Salary");
        allCriteria.add("Work/Life Balance");
        allCriteria.add("Lab Score");
        allCriteria.add("Professional Development");

        binding.evaluationResultToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (researchTitle != null) {
            baseRepository.getUniversityByResearchTitle(researchTitle).observe(this, new Observer<List<University>>() {
                @Override
                public void onChanged(List<University> universities) {
                    if (universities != null) {
                        showProgressIndicator(false);

                        List<Criteria> criteriaList = calculateCriteria(universities, researchTitle, maxSalary, facilityScore, workLifeBalance, professionalDev);
                        criteriaAdapter.setList(criteriaList);

                        if (universities.size() == 0) {
                            binding.resultEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            binding.resultEmptyState.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }


    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.evaluationResultRecyclerview.setLayoutManager(llm);
        binding.evaluationResultRecyclerview.setHasFixedSize(true);
        criteriaAdapter = new CriteriaAdapter(new CriteriaAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Criteria criteria) {
                List<Integer> selectedIndex = criteriaHashmap.get(criteria.getUniversity().getUniversityId());
                Intent i = new Intent(ProfessorEvaluationListActivity.this, ProfessorEvaluationSummaryActivity.class);
                i.putExtra(Constants.KEY_USER_TYPE, userType);
                i.putExtra("UniversityId", criteria.getUniversity().getUniversityId());
                i.putExtra("CriteriaScore", criteria.getScore());
                i.putExtra("ResearchTitle", researchTitle);
                i.putIntegerArrayListExtra("SelectedIndexList", (ArrayList<Integer>) selectedIndex);
                i.putStringArrayListExtra("AllCriteria", (ArrayList<String>) allCriteria);
                i.putParcelableArrayListExtra("EvaluationSummary", (ArrayList<? extends Parcelable>) mapEntries(criteria.getUniversity().getUniversityResearch(),
                        criteria.getUniversity().getUniversityCriteriaScore().getWorkLifeBalance(), criteria.getUniversity().getUniversityCriteriaScore().getFacilityScore(),
                        criteria.getUniversity().getUniversityCriteriaScore().isProfessionalDevelopment(), researchTitle, allCriteria, selectedIndex));
                startActivity(i);
            }
        });
        binding.evaluationResultRecyclerview.setAdapter(criteriaAdapter);
    }

    public List<Boolean> convertIndexToBoolean(List<String> criteriaList, List<Integer> selectedCriteriaIndex) {
        List<Boolean> indexList = new ArrayList<>();

        //fill array entry with false value
        boolean[] itemChecked = new boolean[criteriaList.size()];
        Arrays.fill(itemChecked, false);

        //converting the local array to list
        for (boolean b : itemChecked) {
            indexList.add(b);
        }

        //replacing specified index with true value
        for (int i : selectedCriteriaIndex) {
            indexList.set(i, true);
        }
        return indexList;
    }

    public List<EvaluationSummary> mapEntries(HashMap<String, Research> research, int uniWorkLifeBalance, int uniFacilityScore, boolean uniProfessionalDev, String researchTitle, List<String> criteriaList, List<Integer> selectedCriteriaIndex) {
        List<EvaluationSummary> evaluationSummaries = new ArrayList<>();
        List<Boolean> indexlist = convertIndexToBoolean(criteriaList, selectedCriteriaIndex);
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(0), String.valueOf(maxSalary), String.valueOf(getSalaryForResearch(research, researchTitle)), indexlist.get(0)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(1), mapWorkLifeBalance(workLifeBalance), mapWorkLifeBalance(uniWorkLifeBalance), indexlist.get(1)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(2), mapLabScore(facilityScore), mapLabScore(uniFacilityScore), indexlist.get(2)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(3), mapProfessionalDev(professionalDev), mapProfessionalDev(uniProfessionalDev), indexlist.get(3)));
        return evaluationSummaries;
    }

    public String mapLabScore(int score) {
        String mScore;
        String[] labScore = getResources().getStringArray(R.array.score);
        if (score == 1) {
            mScore = labScore[0];
        } else if (score == 2) {
            mScore = labScore[1];
        } else if (score == 3) {
            mScore = labScore[2];
        } else if (score == 4) {
            mScore = labScore[3];
        } else {
            mScore = labScore[4];
        }
        return mScore;
    }

    public String mapWorkLifeBalance(int score) {
        String mScore;
        String[] workLifeBalance = getResources().getStringArray(R.array.score);
        if (score == 1) {
            mScore = workLifeBalance[0];
        } else if (score == 2) {
            mScore = workLifeBalance[1];
        } else if (score == 3) {
            mScore = workLifeBalance[2];
        } else if (score == 4) {
            mScore = workLifeBalance[3];
        } else {
            mScore = workLifeBalance[4];
        }
        return mScore;
    }

    public String mapProfessionalDev(boolean choice) {
        String mChoice;
        String[] professionalDev = getResources().getStringArray(R.array.choice);
        if (choice) {
            mChoice = professionalDev[0];
        } else {
            mChoice = professionalDev[1];
        }
        return mChoice;
    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
        }
    }

    public List<Criteria> calculateCriteria(List<University> universities, String researchTitle, long maxSalary, int facilityScore, int workLifeBalance, boolean professionalDev) {

        List<Criteria> criteria = new ArrayList<>();
        int criteriaScore = 0;

        for (University u : universities) {
            List<Integer> passedCriteria = new ArrayList<>();
            if (filterByMaxSalary(u, maxSalary, researchTitle)) {
                criteriaScore = criteriaScore + 1;
                passedCriteria.add(0);
                Log.i("criteria score for elements", "salary added");
            }
            if (filterWorkLifeBalance(u, workLifeBalance)) {
                criteriaScore = criteriaScore + 1;
                passedCriteria.add(1);
                Log.i("criteria score for elements", "worklife added");
            }
            if (filterFacilityScore(u, facilityScore)) {
                criteriaScore = criteriaScore + 1;
                passedCriteria.add(2);
                Log.i("criteria score for elements", "facility score added");
            }
            if (filterProfessionalDev(u, professionalDev)) {
                criteriaScore = criteriaScore + 1;
                passedCriteria.add(3);
                Log.i("criteria score for elements", "professional dev added");
            }
            Log.i("criteria score for elements", String.valueOf(criteriaScore));

            criteriaHashmap.put(u.getUniversityId(), passedCriteria);
            criteria.add(new Criteria(u, criteriaScore));
            criteriaScore = 0;
        }

        Collections.sort(criteria, new Comparator<Criteria>() {
            @Override
            public int compare(Criteria o1, Criteria o2) {
                return o1.getScore() - (o2.getScore());
            }
        });

        Collections.reverse(criteria);

        return criteria;
    }


    // PROFESSIONAL DEV
    public boolean filterProfessionalDev(University university, boolean filterParameter) {
        boolean value = false;
        if (university.getUniversityCriteriaScore().isProfessionalDevelopment() == filterParameter) {
            value = true;
        }
        return value;
    }

    // WORK LIFE
    public boolean filterWorkLifeBalance(University university, int filterParameter) {
        boolean value = false;
        // WorkLife Balance
        if (university.getUniversityCriteriaScore().getWorkLifeBalance() >= (filterParameter)) {
            value = true;
        }
        return value;
    }

    // FACILITY SCORE
    public boolean filterFacilityScore(University university, int filterParameter) {
        boolean value = false;
        // FacilityScore
        if (university.getUniversityCriteriaScore().getFacilityScore() >= (filterParameter)) {
            value = true;
        }

        return value;
    }

    public boolean filterByMaxSalary(University university, long maxSalary, String researchTitle) {
        boolean value = false;
        long researchSalary = getSalaryForResearch(university.getUniversityResearch(), researchTitle);
        if (Long.compare(researchSalary, maxSalary) == -1) {
            value = true;
        }
        return value;
    }

    public long getSalaryForResearch(HashMap<String, Research> researchHashMap, String researchTitle) {
        long researchSalary = 0;
        List<Research> researchList = convertResearchHashmapToList(researchHashMap);
        for (Research c : researchList) {
            if (c.getResearchTitle().equals(researchTitle)) {
                researchSalary = Long.parseLong(c.getResearchSalary());
            }
        }
        return researchSalary;
    }

    public List<Research> convertResearchHashmapToList(HashMap<String, Research> researchHashmap) {
        return new ArrayList<>(researchHashmap.values());
    }

}