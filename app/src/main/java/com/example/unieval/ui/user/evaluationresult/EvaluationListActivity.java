package com.example.unieval.ui.user.evaluationresult;

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
import com.example.unieval.data.pojo.Course;
import com.example.unieval.data.pojo.Criteria;
import com.example.unieval.data.pojo.EvaluationSummary;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.ActivityEvaluationListBinding;
import com.example.unieval.ui.CriteriaAdapter;
import com.example.unieval.ui.user.evaluationsummary.EvaluationSummaryActivity;
import com.example.unieval.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EvaluationListActivity extends AppCompatActivity {

    ActivityEvaluationListBinding binding;
    BaseRepository baseRepository;
    CriteriaAdapter criteriaAdapter;

    String userType;

    String courseName;
    long maxFees;
    int facilityScore, studentTeacherRatio, gradEmployment;
    boolean internship;

    HashMap<String, List<Integer>> criteriaHashmap;
    List<String> allCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_list);

        criteriaHashmap = new HashMap<>();
        allCriteria = new ArrayList<>();

        userType = getIntent().getStringExtra(Constants.KEY_USER_TYPE);
        courseName = getIntent().getStringExtra("CourseTitle");
        maxFees = getIntent().getLongExtra("MaxFees", 0);
        facilityScore = getIntent().getIntExtra("FacilityScore", 0);
        studentTeacherRatio = getIntent().getIntExtra("TeacherStudentRatio", 0);
        gradEmployment = getIntent().getIntExtra("GradEmploymentPercent", 0);
        internship = getIntent().getBooleanExtra("Internship", false);

        baseRepository = new BaseRepository();
        initAdapter();

        allCriteria.add("Fees");
        allCriteria.add("Facility Score");
        allCriteria.add("Teacher/Student Ratio");
        allCriteria.add("Graduate/Employment Percentage");
        allCriteria.add("Internship");

        binding.evaluationResultToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (courseName != null) {
            showProgressIndicator(true);
            baseRepository.getUniversityByCourseTitle(courseName).observe(this, new Observer<List<University>>() {
                @Override
                public void onChanged(List<University> universities) {
                    if (universities != null) {
                        showProgressIndicator(false);
                        List<Criteria> criteriaList = calculateCriteria(universities, courseName, maxFees, facilityScore, studentTeacherRatio, gradEmployment, internship);
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
                Intent i = new Intent(EvaluationListActivity.this, EvaluationSummaryActivity.class);
                i.putExtra(Constants.KEY_USER_TYPE, userType);
                i.putExtra("UniversityId", criteria.getUniversity().getUniversityId());
                i.putExtra("CriteriaScore", criteria.getScore());
                i.putExtra("CourseTitle", courseName);
                i.putIntegerArrayListExtra("SelectedIndexList", (ArrayList<Integer>) selectedIndex);
                i.putStringArrayListExtra("AllCriteria", (ArrayList<String>) allCriteria);
                i.putParcelableArrayListExtra("EvaluationSummary", (ArrayList<? extends Parcelable>) mapEntries(criteria.getUniversity().getUniversityCourse(), criteria.getUniversity().getUniversityCriteriaScore().getFacilityScore(),
                        criteria.getUniversity().getUniversityCriteriaScore().getStudentTeacherRatio(), criteria.getUniversity().getUniversityCriteriaScore().getGraduateEmploymentRatio(), criteria.getUniversity().getUniversityCriteriaScore().isInternship(), courseName, allCriteria, selectedIndex));
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

    public List<EvaluationSummary> mapEntries(HashMap<String, Course> courses, int uniFacilityScore, int uniStudentTeacher, int uniEmploymentRatio, boolean uniInternship, String courseName, List<String> criteriaList, List<Integer> selectedCriteriaIndex) {
        List<EvaluationSummary> evaluationSummaries = new ArrayList<>();
        List<Boolean> indexlist = convertIndexToBoolean(criteriaList, selectedCriteriaIndex);
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(0), String.valueOf(maxFees), String.valueOf(getFeesForCourse(courses, courseName)), indexlist.get(0)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(1), mapFacilityScore(facilityScore), mapFacilityScore(uniFacilityScore), indexlist.get(1)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(2), mapStudentTeacherRatio(studentTeacherRatio), mapStudentTeacherRatio(uniStudentTeacher), indexlist.get(2)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(3), mapGradEmployment(gradEmployment), mapGradEmployment(uniEmploymentRatio), indexlist.get(3)));
        evaluationSummaries.add(new EvaluationSummary(criteriaList.get(4), mapInternship(internship), mapInternship(uniInternship), indexlist.get(4)));
        return evaluationSummaries;
    }

    public String mapFacilityScore(int score) {
        String mScore;
        String[] facilityScore = getResources().getStringArray(R.array.score);
        if (score == 1) {
            mScore = facilityScore[0];
        } else if (score == 2) {
            mScore = facilityScore[1];
        } else if (score == 3) {
            mScore = facilityScore[2];
        } else if (score == 4) {
            mScore = facilityScore[3];
        } else {
            mScore = facilityScore[4];
        }
        return mScore;
    }

    public String mapStudentTeacherRatio(int ratio) {
        String mRatio;
        String[] studentTeacherRatio = getResources().getStringArray(R.array.ratio);
        if (ratio == 1) {
            mRatio = studentTeacherRatio[0];
        } else if (ratio == 2) {
            mRatio = studentTeacherRatio[1];
        } else if (ratio == 3) {
            mRatio = studentTeacherRatio[2];
        } else if (ratio == 4) {
            mRatio = studentTeacherRatio[3];
        } else {
            mRatio = studentTeacherRatio[4];
        }
        return mRatio;
    }

    public String mapGradEmployment(int percent) {
        String mPercent;
        String[] graduateEmploymentPercent = getResources().getStringArray(R.array.percent);
        if (percent == 1) {
            mPercent = graduateEmploymentPercent[0];
        } else if (percent == 2) {
            mPercent = graduateEmploymentPercent[1];
        } else if (percent == 3) {
            mPercent = graduateEmploymentPercent[2];
        } else {
            mPercent = graduateEmploymentPercent[3];
        }
        return mPercent;
    }

    public String mapInternship(boolean choice) {
        String mChoice;
        String[] internship = getResources().getStringArray(R.array.choice);
        if (choice) {
            mChoice = internship[0];
        } else {
            mChoice = internship[1];
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

    public List<Criteria> calculateCriteria(List<University> universities, String courseTitle, long maxFees, int facilityScore, int studentTeacherRatio, int gradEmployment, boolean internship) {

        List<Criteria> criteria = new ArrayList<>();
        int criteriaScore = 0;

        for (University u : universities) {
            List<Integer> passedCriteriaIndex = new ArrayList<>();
            if (filterByMaxFees(u, maxFees, courseTitle)) {
                criteriaScore = criteriaScore + 1;
                passedCriteriaIndex.add(0);
                Log.i("criteria score for elements", "fees added");
            }
            if (filterFacilityScore(u, facilityScore)) {
                criteriaScore = criteriaScore + 1;
                passedCriteriaIndex.add(1);
                Log.i("criteria score for elements", "facility score added");
            }
            if (filterTeacherStudentRatio(u, studentTeacherRatio)) {
                criteriaScore = criteriaScore + 1;
                passedCriteriaIndex.add(2);
                Log.i("criteria score for elements", "teacherStudent added");
            }
            if (filterGradEmployment(u, gradEmployment)) {
                criteriaScore = criteriaScore + 1;
                passedCriteriaIndex.add(3);
                Log.i("criteria score for elements", "gradEmployment added");
            }
            if (filterInternship(u, internship)) {
                criteriaScore = criteriaScore + 1;
                passedCriteriaIndex.add(4);
                Log.i("criteria score for elements", "internship added");
            }
            Log.i("criteria score", String.valueOf(criteriaScore));
            Log.i("passed criteria size", String.valueOf(passedCriteriaIndex.size()));

            criteriaHashmap.put(u.getUniversityId(), passedCriteriaIndex);
            criteria.add(new Criteria(u, criteriaScore));
            criteriaScore = 0;
        }

        Collections.sort(criteria, new Comparator<Criteria>() {
            @Override
            public int compare(Criteria o1, Criteria o2) {
                return o1.getScore() - o2.getScore();
            }
        });
        Collections.reverse(criteria);

        return criteria;
    }


    // FEES
    public boolean filterByMaxFees(University university, long maxPrice, String courseTitle) {
        boolean value = false;
        long coursePrice = getFeesForCourse(university.getUniversityCourse(), courseTitle);
        if (Long.compare(coursePrice, maxPrice) == -1) {
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

    // TEACHER/STUDENT RATIO
    public boolean filterTeacherStudentRatio(University university, int filterParameter) {
        boolean value = false;
        // TeacherStudentRatio
        if (university.getUniversityCriteriaScore().getStudentTeacherRatio() <= (filterParameter)) {
            value = true;
        }
        return value;
    }

    // GRAD EMPLOYMENT
    public boolean filterGradEmployment(University university, int filterParameter) {
        boolean value = false;
        // GraduateEmploymentPercent
        if (university.getUniversityCriteriaScore().getGraduateEmploymentRatio() >= (filterParameter)) {
            value = true;
        }
        return value;
    }

    // INTERNSHIP
    public boolean filterInternship(University university, boolean filterParameter) {
        boolean value = false;
        if (university.getUniversityCriteriaScore().isInternship() == filterParameter) {
            value = true;
        }
        return value;
    }

    public long getFeesForCourse(HashMap<String, Course> courseHashMap, String courseTitle) {
        long courseFees = 0;
        List<Course> courseList = convertHashmapToList(courseHashMap);
        for (Course c : courseList) {
            if (c.getCourseTitle().equals(courseTitle)) {
                courseFees = Long.parseLong(c.getCoursePrice());
            }
        }
        return courseFees;
    }

    public List<Course> convertHashmapToList(HashMap<String, Course> courseHashmap) {
        return new ArrayList<>(courseHashmap.values());
    }


}