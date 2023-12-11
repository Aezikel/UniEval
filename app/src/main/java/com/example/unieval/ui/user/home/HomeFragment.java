package com.example.unieval.ui.user.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.FragmentHomeBinding;
import com.example.unieval.ui.UniversityAdapter;
import com.example.unieval.ui.user.UserMainActivity;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.example.unieval.util.Constants;

import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    UniversityAdapter universityAdapter;
    BaseRepository baseRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        baseRepository = new BaseRepository();
        initAdapter();

        baseRepository.getAllUniversity().observe(getViewLifecycleOwner(), new Observer<List<University>>() {
            @Override
            public void onChanged(List<University> universities) {
                if (universities != null) {
                    universityAdapter.setList(universities);
                    if (universities.size() == 0) {
                        binding.userHomeEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        binding.userHomeEmptyState.setVisibility(View.GONE);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.fragmentHomeRecyclerView.setLayoutManager(llm);
        binding.fragmentHomeRecyclerView.setHasFixedSize(true);
        universityAdapter = new UniversityAdapter(new UniversityAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String universityId) {
                // use nav controller to get activity if it doesn't work
                UserMainActivity activity = (UserMainActivity) getActivity();
                String userType = activity.userType;
                Intent i = new Intent(getActivity(), UniversityDetailActivity.class);
                i.putExtra(Constants.KEY_INTENT_UNIVERSITY_ID, universityId);
                i.putExtra(Constants.KEY_USER_TYPE, userType);
                startActivity(i);
            }
        });
        binding.fragmentHomeRecyclerView.setAdapter(universityAdapter);
    }

}