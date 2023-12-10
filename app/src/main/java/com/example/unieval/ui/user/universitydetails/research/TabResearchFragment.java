package com.example.unieval.ui.user.universitydetails.research;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.Research;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.FragmentTabResearchBinding;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.example.unieval.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabResearchFragment extends Fragment {

    FragmentTabResearchBinding binding;
    ResearchExpandableListAdapter researchExpandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Research>> expandableListDetail;
    String universityId;
    BaseRepository baseRepository;

    public TabResearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_research, container, false);

        baseRepository = new BaseRepository();

        UniversityDetailActivity activity = (UniversityDetailActivity) getActivity();
        universityId = activity.universityId;

        if (universityId != null) {
            baseRepository.getUniversity(universityId).observe(getViewLifecycleOwner(), new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        expandableListDetail = createHashmap(convertResearchHashmapToList(university.getUniversityResearch()));
                        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                        researchExpandableListAdapter = new ResearchExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
                        binding.expandableListView.setAdapter(researchExpandableListAdapter);
                    }
                }
            });
        }

        return binding.getRoot();
    }

    public List<Research> segmentList(List<Research> masterList, String discipline) {
        List<Research> disciplineList = new ArrayList<>();
        for (Research c : masterList) {
            if (c.getResearchDiscipline().equals(discipline)) {
                // create discipline list
                disciplineList.add(c);
            }
        }
        return disciplineList;
    }

    public HashMap<String, List<Research>> createHashmap(List<Research> masterList) {
        HashMap<String, List<Research>> expandableListDetail = new HashMap<>();

        List<Research> engineering = segmentList(masterList, Constants.DISCIPLINE_ENGINEERING);
        List<Research> art = segmentList(masterList, Constants.DISCIPLINE_ART);
        List<Research> science = segmentList(masterList, Constants.DISCIPLINE_SCIENCE);
        List<Research> business = segmentList(masterList, Constants.DISCIPLINE_BUSINESS);

        expandableListDetail.put("Engineering", engineering);
        expandableListDetail.put("Art and Humanities", art);
        expandableListDetail.put("Science", science);
        expandableListDetail.put("Business", business);

        return expandableListDetail;
    }

    public List<Research> convertResearchHashmapToList(HashMap<String, Research> researchHashmap) {
        return new ArrayList<>(researchHashmap.values());
    }

}