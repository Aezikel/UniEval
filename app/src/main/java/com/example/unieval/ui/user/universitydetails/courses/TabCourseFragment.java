package com.example.unieval.ui.user.universitydetails.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Course;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.FragmentTabCourseBinding;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.example.unieval.util.Constants;

public class TabCourseFragment extends Fragment {

    FragmentTabCourseBinding binding;
    CourseExpandableListAdapter courseExpandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Course>> expandableListDetail;
    String universityId;
    BaseRepository baseRepository;

    public TabCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_course, container, false);

        baseRepository = new BaseRepository();

        UniversityDetailActivity activity = (UniversityDetailActivity) getActivity();
        universityId = activity.universityId;

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(getViewLifecycleOwner(), new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        expandableListDetail = createHashmap(convertHashmapToList(university.getUniversityCourse()));
                        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                        courseExpandableListAdapter = new CourseExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
                        binding.expandableListView.setAdapter(courseExpandableListAdapter);
                    }
                }
            });
        }
        return binding.getRoot();
    }

    public List<Course> segmentList(List<Course> masterList, String discipline) {
        List<Course> disciplineList = new ArrayList<>();

        for (Course c : masterList) {
            if (c.getCourseDiscipline().equals(discipline)) {
                // create discipline list
                disciplineList.add(c);
            }
        }
        return disciplineList;
    }

    public HashMap<String, List<Course>> createHashmap(List<Course> masterList) {
        HashMap<String, List<Course>> expandableListDetail = new HashMap<>();

        List<Course> engineering = segmentList(masterList, Constants.DISCIPLINE_ENGINEERING);
        List<Course> art = segmentList(masterList, Constants.DISCIPLINE_ART);
        List<Course> science = segmentList(masterList, Constants.DISCIPLINE_SCIENCE);
        List<Course> business = segmentList(masterList, Constants.DISCIPLINE_BUSINESS);

        expandableListDetail.put("Engineering", engineering);
        expandableListDetail.put("Art and Humanities", art);
        expandableListDetail.put("Science", science);
        expandableListDetail.put("Business", business);


        return expandableListDetail;
    }

    public List<Course> convertHashmapToList(HashMap<String, Course> courseHashmap) {
        return new ArrayList<>(courseHashmap.values());
    }

}